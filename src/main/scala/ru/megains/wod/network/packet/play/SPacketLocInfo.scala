package ru.megains.wod.network.packet.play

import ru.megains.wod.location.Location
import ru.megains.wod.network.packet.{PacketBufferS, PacketWrite}

class SPacketLocInfo(loc:Location) extends PacketWrite{



//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBufferS): Unit = {
        loc.write(buf)
    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
