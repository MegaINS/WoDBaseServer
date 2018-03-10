package ru.megains.wod.network.packet

abstract class PacketWrite extends Packet {

    def writePacketData(buf: PacketBuffer): Unit

}
