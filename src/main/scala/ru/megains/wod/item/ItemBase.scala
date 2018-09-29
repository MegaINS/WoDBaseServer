package ru.megains.wod.item

import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemAction.ItemAction
import ru.megains.wod.item.ItemParam.ItemParam

import scala.collection.mutable

class ItemBase(val id:Int, val name:String, val img:String, val level:Int, val cost:Int, val weight:Boolean, val `private`:Boolean, val action:ItemAction, val slot:SlotType, val stack:Boolean) {
    val itemParams: mutable.HashMap[ItemParam,Int] = Items.loadParams(id)
}



