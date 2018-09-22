package ru.megains.wod.store

import ru.megains.wod.item.ItemBase

import scala.collection.mutable.ArrayBuffer

class StoreTab(val id:Int, val name:String) {

    val items = new ArrayBuffer[ItemBase]

}
