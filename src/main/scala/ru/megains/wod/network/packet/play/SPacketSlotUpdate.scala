package ru.megains.wod.network.packet.play

import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketSlotUpdate(slotType: SlotType, item:ItemUser = null) extends PacketWrite{


    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(slotType.id)
        val isNotNull = item!=null
        buf.writeBoolean(isNotNull)
        if(isNotNull){
            buf.writeItemUser(item)
        }

    }
}
