package ru.megains.wod.network.packet.play

import ru.megains.wod.inventory.InventoryType.InventoryType
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketInvUpdate(invTupe:InventoryType,itemsIn:Array[ItemUser],test:Byte) extends PacketWrite{


//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeByte(test)
        buf.writeByte(invTupe.id)
        buf.writeShort(itemsIn.length)
        for (item <- itemsIn){
            buf.writeItemUser(item)
        }


    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
