package ru.megains.wod.network.packet

import ru.megains.wod.network.handler.INetHandler


abstract class PacketRead[T <: INetHandler] extends Packet[T]{

    def readPacketData(buf: PacketBuffer): Unit

    def processPacket(handler: T): Unit
}
