package ru.megains.wod.entity

import ru.megains.wod.network.handler.INetHandler
import ru.megains.wod.network.packet.Packet

abstract class Entity {


    def sendPacket(packetIn: Packet[_ <: INetHandler]): Unit = {}
}
