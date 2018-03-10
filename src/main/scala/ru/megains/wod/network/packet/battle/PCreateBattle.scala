package ru.megains.wod.network.packet.battle

import ru.megains.wod.entity.EntityType.EntityType
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class PCreateBattle(id:Int, id1:Int,et1:EntityType,id2:Int,et2:EntityType) extends PacketWrite{



//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(id)
        buf.writeInt(id1)
        buf.writeInt(id2)
        buf.writeByte(et1.id)
        buf.writeByte(et2.id)
    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
