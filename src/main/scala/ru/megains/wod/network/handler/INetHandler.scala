package ru.megains.wod.network.handler

import ru.megains.wod.network.packet.Packet

abstract class INetHandler {


    def onDisconnect(msg: String)

    def disconnect(msg: String)

    def sendPacket(packetIn: Packet[_ <: INetHandler]) {}

}
