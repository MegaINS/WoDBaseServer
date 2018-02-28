package ru.megains.wod.network.packet.battle

import ru.megains.wod.network.handler.NetHandlerBattleServer
import ru.megains.wod.network.packet.{Packet, PacketBuffer}

class PBattleStatus extends Packet[NetHandlerBattleServer]{

        var id:Int = -1
    var status:Int = -1
    override def readPacketData(buf: PacketBuffer): Unit = {
        id = buf.readInt()
        status = buf.readByte()
    }

    override def writePacketData(buf: PacketBuffer): Unit = {

    }

    override def processPacket(handler: NetHandlerBattleServer): Unit = {
        handler.processBattleStatus(this)
    }
}
