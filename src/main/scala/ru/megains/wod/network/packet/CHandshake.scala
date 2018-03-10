package ru.megains.wod.network.packet

import ru.megains.wod.network.handler.NetHandlerHandshakeTCP

class CHandshake() extends PacketRead[NetHandlerHandshakeTCP] {

    var connectionState: ConnectionState = _

    override def isImportant: Boolean = true

    def this(version: Int, ip: String, port: Int, connectionState: ConnectionState) {
        this()
        this.connectionState = connectionState
    }

    override def readPacketData(packetBuffer: PacketBuffer): Unit = {
        connectionState = ConnectionState.getFromId(packetBuffer.readByte())
    }

//    override def writePacketData(packetBuffer: PacketBuffer): Unit = {
//        packetBuffer.writeInt(connectionState.id)
//    }


    def hasPriority: Boolean = true


    override def processPacket(handler: NetHandlerHandshakeTCP) {
        handler.processHandshake(this)
    }
}
