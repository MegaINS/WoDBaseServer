package ru.megains.wod.item

import ru.megains.wod.item.ItemAction.ItemAction
import ru.megains.wod.entity.player.BodySlot.BodySlot

class ItemUser(val id:Int,baseId:Int, val place:String,var amount:Int) {

    val itemBase: Item = Items.getItem(baseId)

    def img = itemBase.img

    def action: ItemAction = itemBase.action

    def name = itemBase.name

    def  slot:BodySlot = itemBase.slot
}
