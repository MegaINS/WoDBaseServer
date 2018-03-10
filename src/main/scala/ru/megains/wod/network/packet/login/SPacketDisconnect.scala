package ru.megains.wod.network.packet.login

import java.io.IOException

import ru.megains.wod.network.handler.INetHandler
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketDisconnect() extends PacketWrite {

    private var reason: String = _

    def this(text: String) {
        this()
        this.reason = text
    }

    /**
      * Reads the raw packet data from the data stream.
      */
    @throws[IOException]
    def readPacketData(buf: PacketBuffer) {
        this.reason = buf.readStringFromBuffer(32767)
    }

    /**
      * Writes the raw packet data to the data stream.
      */
    @throws[IOException]
    def writePacketData(buf: PacketBuffer) {
        buf.writeStringToBuffer(reason)
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandler) {
       // handler.handleDisconnect(this)
    }


}
