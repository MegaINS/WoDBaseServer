package ru.megains.wod.entity.player

import ru.megains.wod.Action
import ru.megains.wod.db.DBPlayerInfo
import ru.megains.wod.entity.Entity
import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.inventory.{InventoryType, PlayerBackpack}
import ru.megains.wod.location.{Location, Locations}
import ru.megains.wod.network.handler.INetHandler
import ru.megains.wod.network.packet.play._
import ru.megains.wod.network.packet.{Packet, Status}

class Player(val id:Int,val name:String) extends Entity {

    var connection:INetHandler = _

    val info:PlayerInfo = DBPlayerInfo.load(this)
    val stat:PlayerStat = new PlayerStat(this)
    val backpack = new PlayerBackpack(this)
    val body = new PlayerBody(this)
    val slots = new PlayerSlots(this)

    private var location:Location = _

    def location(locationIn:Location = null): Location ={
        if(locationIn!=null){
            location = locationIn
            sendPacket(new SPacketLocInfo(location))
            val a =System.currentTimeMillis()
            DBPlayerInfo.updateLocation(id,location.id)
            println(System.currentTimeMillis() - a)
        }
        location
    }




    def load(info: PlayerInfo): Unit ={
        location = Locations.getLocation(info.locationIn)

    }

    def sendData(): Unit ={
        sendPacket(new SPacketPlayerInfo(this))
        sendPacket(new SPacketInventory(InventoryType.backpack, backpack))
        sendPacket(new SPacketBody(body))
        sendPacket(new SPacketSlots(slots))
        location.enter(this)
    }
    def takeOff(slot: SlotType): Unit = {
        slot match {
            case SlotType.none =>
                throw new Exception("SlotType.none")
            case SlotType.elixir =>
                //slots.takeOff(id)
                println("SlotType.elixir")
            case _ =>

                val item = body.getItemInSlot(slot)
                if(item != null){
                    body.setSlot(slot)
                    backpack.addItem(item)
                    sendPacket(new SPacketActionReturn(Status.success,Action.takeOff,slot.id))
                }
        }
    }

    def take(id: Int): Unit = {
        val optItem = backpack.getItem(id)

        optItem match {
            case Some(item) =>
              item.slot match{
                  case SlotType.elixir =>
//                      val value:Int = slots.take(item)
//                      if(value > 0){
//                          backpack.removeItem(id,value)
//                      }
                  case SlotType.none =>
                      throw new Exception("SlotType.none")
                  case _ =>
                      val slot = item.slot
                      val available = true
                      if(available){
                          takeOff(slot)
                          if(body.getItemInSlot(slot)==null){
                              body.setSlot(slot,item)
                              backpack.removeItem(id)
                              sendPacket(new SPacketActionReturn(Status.success,Action.take,item.id))
                          }
                      }
              }
            case _ =>
        }
    }


    def setConnection(connectionIn: INetHandler): Unit = {
        if(connection != null){
            connection.disconnect("reLogin")
        }
        connection = connectionIn
    }
   override def sendPacket(packetIn: Packet[_ <: INetHandler]): Unit = {
        connection.sendPacket(packetIn)
    }
}
