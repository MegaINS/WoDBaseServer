package ru.megains.wod.network.packet.play

import ru.megains.wod.network.packet.{Packet, PacketBuffer}

class SPacketStartBattle(id:Int) extends Packet{


    override def readPacketData(buf: PacketBuffer): Unit = {

    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(id)
    }

    override def processPacket(handler: Nothing): Unit = {

    }
}