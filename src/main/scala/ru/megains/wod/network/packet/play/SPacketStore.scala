package ru.megains.wod.network.packet.play

import ru.megains.wod.network.packet.{PacketBufferS, PacketWrite}
import ru.megains.wod.store.Store

class SPacketStore(store:Store) extends PacketWrite{


//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBufferS): Unit = {

        store.write(buf)

    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
