package ru.megains.wod.network.packet.play

import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}
import ru.megains.wod.store.Store

class SPacketStore(store:Store) extends PacketWrite{


//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBuffer): Unit = {

        store.write(buf)

    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
