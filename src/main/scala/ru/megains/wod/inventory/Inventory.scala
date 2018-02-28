package ru.megains.wod.inventory

import ru.megains.wod.entity.db.WoDDatabase
import ru.megains.wod.item.ItemUser

import scala.collection.mutable

abstract class Inventory {

    val db = WoDDatabase.db
    val items = new mutable.HashMap[Int,ItemUser]()



    def addItem(item: ItemUser): Unit = {
        if (item.itemBase.stack) {

        } else {
            items += item.id -> item
        }
    }


    def getItemFromId(id: Int): ItemUser = {
        items.getOrElse(id,default = null)
    }

    def removeItem(id: Int): Unit = {
        items -= id
    }




}
