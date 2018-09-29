package ru.megains.wod.network.packet.login

import ru.megains.wod.network.handler.INetHandlerLoginServer
import ru.megains.wod.network.packet.{PacketBufferS, PacketRead}

class CPacketLoginStart extends PacketRead[INetHandlerLoginServer] {


    var email: String = ""
    var pass: String = ""

    override def readPacketData(packetBuffer: PacketBufferS): Unit = {
        email = packetBuffer.readStringFromBuffer(255)
        pass = packetBuffer.readStringFromBuffer(255)
    }

//    override def writePacketData(packetBuffer: PacketBuffer): Unit = {
//
//    }

    override def processPacket(handler: INetHandlerLoginServer): Unit = {
        handler.processLoginStart(this)
    }
}
