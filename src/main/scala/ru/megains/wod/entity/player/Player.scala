package ru.megains.wod.entity.player

import anorm.SQL
import ru.megains.wod.Action
import ru.megains.wod.entity.Entity
import ru.megains.wod.db.WoDDatabase
import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.inventory.{InventoryBackpack, InventoryType}
import ru.megains.wod.item.ItemBase
import ru.megains.wod.location.{Location, Locations}
import ru.megains.wod.network.handler.INetHandler
import ru.megains.wod.network.packet.play._
import ru.megains.wod.network.packet.{Packet, Status}

class Player( val id:Int,val name:String) extends Entity {

    val db = WoDDatabase.db
    var connection:INetHandler = _
    private var location:Location = _
    val backpack = new InventoryBackpack(this)
    val body = new PlayerBody(this)
    val slots = new PlayerSlots(this)
    var level:Int = 0
    var money:Int = 0
    var exp:Int = 0


    def location(locationIn:Location = null): Location ={
        if(locationIn!=null){
            location = locationIn
            sendPacket(new SPacketLocInfo(location))
            db.withConnection(implicit c =>
                SQL(s"UPDATE users_info SET location='${location.id}' WHERE id='$id'").execute()
            )
        }
        location
    }




    def load(info: PlayerInfo): Unit ={
        level = info.levelIn
        money = info.moneyIn
        exp = info.expIn
        location =   Locations.getLocation(info.locationIn)

        backpack.load()
        body.load()
        slots.load()
    }

    def sendData(): Unit ={
        sendPacket(new SPacketPlayerInfo(this))
        sendPacket(new SPacketInventory(InventoryType.backpack, backpack))
        sendPacket(new SPacketBody(body))
        sendPacket(new SPacketSlots(slots))
        location.enter(this)
    }
    def takeOff(slot: SlotType,id:Int): Unit = {
        slot match {
            case SlotType.none =>
                throw new Exception("SlotType.none")
            case SlotType.elixir =>
                slots.takeOff(id)
                println("SlotType.elixir")
            case _ =>
                val item = body.getItemInSlot(slot)
                if(item ne null){

                    body.setSlot(slot,null)
                    backpack.addItem(item)
                    sendPacket(new SPacketActionReturn(Status.success,Action.takeOff,slot.id))
                }
        }
    }

    def take(id: Int): Unit = {
        val itemBase:ItemBase = backpack.getItemBaseFromId(id)
        itemBase.slot match {
            case SlotType.none =>
                throw new Exception("SlotType.none")
            case SlotType.elixir =>
                slots.take(id)
            case _ =>
                takeOff(itemBase.slot,0)
                val item = backpack.getItemFromId(id)
                body.setSlot(itemBase.slot,item)
                sendPacket(new SPacketActionReturn(Status.success,Action.take,item.id))
        }

    }


    def setConnection(connectionIn: INetHandler): Unit = {
        if(connection ne null){
            connection.disconnect("reLogin")
        }
        connection = connectionIn
    }
   override def sendPacket(packetIn: Packet[_ <: INetHandler]): Unit = {
        connection.sendPacket(packetIn)
    }
}
