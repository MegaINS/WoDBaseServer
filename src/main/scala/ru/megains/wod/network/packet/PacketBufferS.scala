package ru.megains.wod.network.packet

import io.netty.buffer.ByteBuf
import ru.megains.wod.item.{ItemBase, ItemUser}

class PacketBufferS(p_i45154_1: ByteBuf) extends PacketBuffer(p_i45154_1){



    def writeItemUser(item: ItemUser): Unit = {
        writeInt(item.id)
        writeStringToBuffer(item.name)
        writeStringToBuffer(item.img)

        writeInt(item.itemBase. itemParams.size)
        item.itemBase.itemParams.foreach{
            case (param,value)=>
                writeInt(param.id)
                writeInt(value)
        }
        writeInt(item.amount)
        writeByte(item.action.id)
        writeByte(item.slot.id)
    }



    def writeItemBase(item: ItemBase): Unit = {
        writeInt(item.id)
        writeStringToBuffer(item.name)
        writeStringToBuffer(item.img)

        writeInt(item.itemParams.size)
        item.itemParams.foreach{
            case (param,value)=>
                writeInt(param.id)
                writeInt(value)
        }
    }
}
