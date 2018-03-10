package ru.megains.wod.network.packet.play

import ru.megains.wod.location.Location
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketLocInfo(loc:Location) extends PacketWrite{



//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        loc.write(buf)
    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
