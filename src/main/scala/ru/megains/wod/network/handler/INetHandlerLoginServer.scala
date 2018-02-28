package ru.megains.wod.network.handler

import ru.megains.wod.network.packet.login.CPacketLoginStart

trait INetHandlerLoginServer extends INetHandler {

    def processLoginStart(packetIn: CPacketLoginStart)

    //  def processEncryptionResponse(packetIn: CPacketEncryptionResponse)
}
