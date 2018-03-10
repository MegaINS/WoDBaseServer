package ru.megains.wod.network.packet.play

import ru.megains.wod.Action
import ru.megains.wod.Action.Action
import ru.megains.wod.entity.player.Player
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketPlayerLocation(action:Action, players: List[Player]) extends PacketWrite{


    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(action.id)
        buf.writeInt(players.length)
        action match {
            case Action.loadPlayerLoc | Action.moveToLcc =>
                players.foreach{
                    player =>
                        buf.writeInt(player.id)
                        buf.writeStringToBuffer(player.name)
                        buf.writeInt(player.level)
                }
            case Action.exitInLoc =>
                players.foreach{
                    player =>
                        buf.writeInt(player.id)
                }
        }
    }


}
