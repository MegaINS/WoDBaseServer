package ru.megains.wod.network.packet.play

import ru.megains.wod.entity.player.PlayerSlots
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketSlots(playerSlots: PlayerSlots) extends PacketWrite{

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(playerSlots.openSlots)
        buf.writeInt(playerSlots.slotsItem.count(_!=null))
        for(i<- 0 until playerSlots.openSlots){
           if(playerSlots.slotsItem(i)!=null){
               buf.writeInt(i)
               buf.writeItemUser(playerSlots.slotsItem(i))
           }
        }
    }
}
