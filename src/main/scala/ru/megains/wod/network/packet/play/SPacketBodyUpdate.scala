package ru.megains.wod.network.packet.play

import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketBodyUpdate(slot:SlotType,item:ItemUser) extends PacketWrite {


    override def writePacketData(buf: PacketBuffer): Unit = {

        buf.writeByte(slot.id)
        val isNotNull = item!=null
        buf.writeBoolean(isNotNull)
        if(isNotNull){
            buf.writeItemUser(item)
        }
    }

}
