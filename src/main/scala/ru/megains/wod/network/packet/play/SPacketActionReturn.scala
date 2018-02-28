package ru.megains.wod.network.packet.play

import ru.megains.wod.Action.Action
import ru.megains.wod.network.packet.Status.Status
import ru.megains.wod.network.packet.{Packet, PacketBuffer}

class SPacketActionReturn(status:Status,action:Action,value:Int) extends Packet{




    override def readPacketData(buf: PacketBuffer): Unit = {

    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeByte(status.id)
        buf.writeByte(action.id)
        buf.writeInt(value)
    }

    override def processPacket(handler: Nothing): Unit = {

    }


}
