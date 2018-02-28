package ru.megains.wod.network.packet.login

import ru.megains.wod.network.handler.INetHandler
import ru.megains.wod.network.packet.{Packet, PacketBuffer}

class SPacketLoginSuccess extends Packet[INetHandler] {


    override def readPacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def writePacketData(packetBuffer: PacketBuffer): Unit = {

    }

//    override def processPacket(handler: INetHandlerLoginClient): Unit = {
//        handler.handleLoginSuccess(this)
//    }
    override def processPacket(handler: INetHandler): Unit = {

}
}
