package ru.megains.wod.network.packet

import ru.megains.wod.network.handler.INetHandler

abstract class Packet[T <: INetHandler] {

    def isImportant = false

    def readPacketData(buf: PacketBuffer): Unit

    def writePacketData(buf: PacketBuffer): Unit

    def processPacket(handler: T): Unit

}



