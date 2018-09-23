package ru.megains.wod.entity.player

import ru.megains.wod.db.{DBPlayerSlot, Database}
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.play.SPacketSlotUpdate

class PlayerSlots(val player: Player) extends Database {

    var openSlots:Int =  DBPlayerSlot.loadOpenSlot(player.id)
    var slotsItem:Array[ItemUser] = DBPlayerSlot.load(player.id)


    def take(id:Int): Unit = {
        for(slotId <- 0 until openSlots ){
            if(slotsItem(slotId) == null){
                val value = 1
                val itemSlot = player.backpack.getItemFromId(id,value)
                slotsItem(slotId) = itemSlot
                DBPlayerSlot.update(player.id,slotId,itemSlot.id)
                player.sendPacket(new SPacketSlotUpdate(slotId,itemSlot))
                return
            }
        }
    }

    def takeOff(id:Int): Unit = {
        for(slotId <- 0 until openSlots ){
            if(slotsItem(slotId) != null &&slotsItem(slotId).id == id){

                val item = slotsItem(slotId)
                slotsItem(slotId) = null
                DBPlayerSlot.update(player.id,slotId,0)
                player.sendPacket(new SPacketSlotUpdate(slotId))
                player.backpack.addItem(item)
                //TODO false


            }
        }

    }
}
