package ru.megains.wod.network.packet.play

import ru.megains.wod.entity.player.{PlayerSlots, SlotType}
import ru.megains.wod.network.packet.{PacketBufferS, PacketWrite}

class SPacketSlots(playerSlots: PlayerSlots) extends PacketWrite{

    override def writePacketData(buf: PacketBufferS): Unit = {
        buf.writeInt(playerSlots.openSlots)
        buf.writeInt(playerSlots.slotsItem.values.count(_!=null))
        for(i<- 1 to playerSlots.openSlots){
           if(playerSlots.slotsItem(SlotType.withName("slot_"+i))!=null){
               buf.writeInt(i)
               buf.writeItemUser(playerSlots.slotsItem(SlotType.withName("slot_"+i)))
           }
        }
    }
}
