package ru.megains.wod.network.packet.play

import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.{PacketBufferS, PacketWrite}

class SPacketSlotUpdate(slotType: SlotType, item:ItemUser = null) extends PacketWrite{


    override def writePacketData(buf: PacketBufferS): Unit = {
        buf.writeInt(slotType.id)
        val isNotNull = item!=null
        buf.writeBoolean(isNotNull)
        if(isNotNull){
            buf.writeItemUser(item)
        }

    }
}
