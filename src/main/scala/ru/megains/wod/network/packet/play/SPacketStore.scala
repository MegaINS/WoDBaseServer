package ru.megains.wod.network.packet.play

import ru.megains.wod.network.packet.{Packet, PacketBuffer}
import ru.megains.wod.store.Store

class SPacketStore(store:Store) extends Packet{


    override def readPacketData(buf: PacketBuffer): Unit = {

    }

    override def writePacketData(buf: PacketBuffer): Unit = {

        store.write(buf)

    }

    override def processPacket(handler: Nothing): Unit = {

    }
}
