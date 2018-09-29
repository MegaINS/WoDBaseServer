package ru.megains.wod.item

import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemAction.ItemAction

class ItemUser(val id:Int,baseId:Int,var amount:Int) {


    val itemBase: ItemBase = Items.getItem(baseId)

    def img: String = itemBase.img

    def action: ItemAction = itemBase.action

    def name: String = itemBase.name

    def slot:SlotType = itemBase.slot
    def get(playerId:Int, value: Int): ItemUser = {
        Items.createItemPlayer(baseId,playerId,value)
    }
}
