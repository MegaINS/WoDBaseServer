package ru.megains.wod.entity.player

import ru.megains.wod.db.{DBPlayerBody, DBPlayerItem}
import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.play.SPacketBodyUpdate

import scala.collection.mutable

class PlayerBody(player: Player) {

    val slots:  mutable.HashMap[SlotType, ItemUser] = DBPlayerBody.load(player.id)

    def getItemInSlot(slot: SlotType): ItemUser = {
        slots(slot)
    }
    def setSlot(slot: SlotType, item: ItemUser = null): Unit = {
        slots(slot) = item
        val itemId = if(item != null) item.id else 0
        DBPlayerBody.updateSlot(player.id,slot,itemId)
        if(itemId > 0){
            DBPlayerItem.updateInventory(itemId,"body")
        }
        player.sendPacket(new SPacketBodyUpdate(slot,item))

    }

}
