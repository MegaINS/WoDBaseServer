package ru.megains.wod.entity.player

import anorm.SQL
import ru.megains.wod.Action
import ru.megains.wod.entity.db.WoDDatabase
import ru.megains.wod.entity.Entity
import ru.megains.wod.inventory.{InventoryBackpack, InventoryType}
import ru.megains.wod.location.{Location, Locations}
import ru.megains.wod.network.handler.INetHandler
import ru.megains.wod.network.packet.{Packet, Status}
import ru.megains.wod.network.packet.play._
import ru.megains.wod.entity.player.BodySlot.BodySlot

class Player( val id:Int,val name:String) extends Entity {

    val db = WoDDatabase.db
    var connection:INetHandler = _
    private var location:Location = _
    val backpack = new InventoryBackpack(this)
    val body = new PlayerBody(this)
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
        location = Locations.getLocation(info.locationIn)
        backpack.load()
        body.load()
    }

    def sendData(): Unit ={
        sendPacket(new SPacketPlayerInfo(this))
        sendPacket(new SPacketLocInfo(location))
        sendPacket(new SPacketInventory(InventoryType.backpack, backpack))
        sendPacket(new SPacketBody(body))
    }
    def takeOff(slot: BodySlot): Unit = {

        val item = body.getItemInSlot(slot)
        if(item ne null){

            body.setSlot(slot,null)
            backpack.addItem(item)
            db.withConnection(implicit c =>
                SQL(s"UPDATE users_items SET place='backpack' WHERE id='${item.id}'").execute()
            )
            sendPacket(new SPacketActionReturn(Status.success,Action.takeOff,slot.id))
        }
    }

    def take(id: Int): Unit = {
        val item = backpack.getItemFromId(id)
        takeOff(item.slot)
        body.setSlot(item.slot,item)
        backpack.removeItem(item.id)
        db.withConnection(implicit c =>
            SQL(s"UPDATE users_items SET place='body' WHERE id='${item.id}'").execute()
        )
        sendPacket(new SPacketActionReturn(Status.success,Action.take,item.id))
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
