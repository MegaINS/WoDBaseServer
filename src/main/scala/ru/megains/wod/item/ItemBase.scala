package ru.megains.wod.item

import ru.megains.wod.item.ItemAction.ItemAction
import ru.megains.wod.entity.player.SlotType.SlotType

class ItemBase(val id:Int, val name:String, val img:String, val tupe:Int, val level:Int, val cost:Int, val weight:Boolean, val privat:Boolean, val action:ItemAction, val slot:SlotType, val stack:Boolean) {

}



