package ru.megains.wod.entity.player

import ru.megains.wod.db.{DBPlayerItem, DBPlayerSlot, Database}
import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.{ItemParam, ItemUser, Items}
import ru.megains.wod.network.packet.play.SPacketSlotUpdate

import scala.collection.mutable

class PlayerSlots(val player: Player) extends Database {

    var openSlots:Int =  DBPlayerSlot.loadOpenSlot(player.id)
    var slotsItem: mutable.HashMap[SlotType, ItemUser] = DBPlayerSlot.load(player.id)


    def take(itemUser: ItemUser): Int = {
        var value = 0
        var item = itemUser
        for(id <- 1 to openSlots ){
            val slotType = SlotType.withName("slot_"+id)
            if(slotsItem(slotType) == null){
                value = itemUser.amount.min(itemUser.itemBase.itemParams(ItemParam.slotSize))
                if(itemUser.amount>1){
                    item = Items.createItemPlayer(itemUser.itemBase.id,player.id,value)

                }

                slotsItem(slotType) = item
                DBPlayerSlot.update(player.id,slotType.toString,item.id)
                DBPlayerItem.updateInventory(item.id,"slot")
                player.sendPacket(new SPacketSlotUpdate(slotType,item))
                return value
            }
        }
        value
    }

    def takeOff(slotType: SlotType): Unit = {

        if(slotsItem(slotType) != null){
            val item = slotsItem(slotType)
            slotsItem(slotType) = null
            DBPlayerSlot.update(player.id,slotType.toString,0)
            player.sendPacket(new SPacketSlotUpdate(slotType))
            player.backpack.addItem(item)
        }
    }
}
