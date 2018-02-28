package ru.megains.wod.network.packet.play

import ru.megains.wod.network.packet.{Packet, PacketBuffer}
import ru.megains.wod.entity.player.Player

class SPacketPlayerInfo(val player: Player) extends Packet{



    override def readPacketData(buf: PacketBuffer): Unit = {

    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(player.id)
        buf.writeStringToBuffer(player.name)
        buf.writeInt(player.level)
        buf.writeInt(player.exp)
        buf.writeInt(player.money)
    }

    override def processPacket(handler: Nothing): Unit = {

    }
}
