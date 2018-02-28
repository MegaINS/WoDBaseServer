package ru.megains.wod.network.packet.play

import ru.megains.wod.location.Location
import ru.megains.wod.network.packet.{Packet, PacketBuffer}

class SPacketLocInfo(loc:Location) extends Packet{



    override def readPacketData(buf: PacketBuffer): Unit = {

    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        loc.write(buf)
    }

    override def processPacket(handler: Nothing): Unit = {

    }
}
