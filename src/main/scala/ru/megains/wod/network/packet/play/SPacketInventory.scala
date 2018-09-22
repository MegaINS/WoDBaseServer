package ru.megains.wod.network.packet.play

import ru.megains.wod.inventory.PlayerBackpack
import ru.megains.wod.inventory.InventoryType.InventoryType
import ru.megains.wod.network.packet.{PacketBuffer, PacketWrite}

class SPacketInventory(invType: InventoryType, inventory: PlayerBackpack) extends PacketWrite{



//    override def readPacketData(buf: PacketBuffer): Unit = {
//
//    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeByte(invType.id)
        buf.writeShort(inventory.items.size)
        inventory.items.foreach{
            case (_,item) =>
                buf.writeItemUser(item)
        }
    }

//    override def processPacket(handler: Nothing): Unit = {
//
//    }
}
