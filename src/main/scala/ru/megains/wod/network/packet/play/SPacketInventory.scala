package ru.megains.wod.network.packet.play

import ru.megains.wod.inventory.InventoryBackpack
import ru.megains.wod.inventory.InventoryType.InventoryType
import ru.megains.wod.network.packet.{Packet, PacketBuffer}

class SPacketInventory(invType: InventoryType, inventory: InventoryBackpack) extends Packet{



    override def readPacketData(buf: PacketBuffer): Unit = {

    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeByte(invType.id)
        buf.writeShort(inventory.items.size)
        inventory.items.foreach{
            case (_,item) =>
                buf.writeItemUser(item)
        }
    }

    override def processPacket(handler: Nothing): Unit = {

    }
}
