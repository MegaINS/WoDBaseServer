package ru.megains.wod.network.packet.play

import ru.megains.wod.entity.player.Player
import ru.megains.wod.network.packet.{PacketBufferS, PacketWrite}

class SPacketPlayerInfo(val player: Player) extends PacketWrite{



    override def writePacketData(buf: PacketBufferS): Unit = {
        buf.writeInt(player.id)
        buf.writeStringToBuffer(player.name)
        buf.writeInt(player.info.levelIn)
        buf.writeInt(player.info.expIn)
        buf.writeInt(player.info.moneyIn)
        buf.writeInt(player.stat.hp)
        buf.writeInt(player.stat.hpMax)
    }


}
