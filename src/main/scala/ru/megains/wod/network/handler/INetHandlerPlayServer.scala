package ru.megains.wod.network.handler

import ru.megains.wod.network.packet.play.CPacketAction

trait INetHandlerPlayServer extends INetHandler {
    def processAction(action: CPacketAction): Unit


}
