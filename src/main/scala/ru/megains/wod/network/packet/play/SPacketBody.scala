package ru.megains.wod.network.packet.play

import ru.megains.wod.network.packet.{Packet, PacketBuffer}
import ru.megains.wod.entity.player.PlayerBody

class SPacketBody(body: PlayerBody) extends Packet{


    override def readPacketData(buf: PacketBuffer): Unit = {

    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        val bodyFilter = body.slots.filter{case(_,item)=> item != null }
        buf.writeByte(bodyFilter.size)
        bodyFilter.foreach{
            case (slot,item)=>
                buf.writeByte(slot.id)
                buf.writeItemUser(item)
        }
    }

    override def processPacket(handler: Nothing): Unit = {

    }
}
