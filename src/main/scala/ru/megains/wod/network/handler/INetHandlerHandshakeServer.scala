package ru.megains.wod.network.handler

import ru.megains.wod.network.packet.CHandshake

trait INetHandlerHandshakeServer extends INetHandler {


    def processHandshake(packet: CHandshake)
}
