package ru.megains.wod.network.packet.play

import ru.megains.wod.entity.player.Player
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketPlayerInfo(val player: Player) extends PacketWrite{



//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(player.id)
        buf.writeStringToBuffer(player.name)
        buf.writeInt(player.level)
        buf.writeInt(player.exp)
        buf.writeInt(player.money)
    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
