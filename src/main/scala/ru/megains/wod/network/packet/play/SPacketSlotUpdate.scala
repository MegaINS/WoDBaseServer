package ru.megains.wod.network.packet.play

import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketSlotUpdate(slot:Int,item:ItemUser = null) extends PacketWrite{


    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(slot)
        buf.writeBoolean(item!=null)
        if(item!=null){
            buf.writeItemUser(item)
        }

    }
}
