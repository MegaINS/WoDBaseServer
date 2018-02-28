package ru.megains.wod.item

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.entity.db.WoDDatabase

object Items {

    val db = WoDDatabase.db
    var idItems = Map.empty[Int,Item]


    def  getItem(id:Int): Item ={
        if(idItems.contains(id)){
            idItems(id)
        }else{
            db.withConnection { implicit c =>
                val item = SQL(s"SELECT * FROM item_base WHERE id='$id'").as(Parsers.itemBase.single)
                idItems += id -> item
                item
            }
        }
    }

    def createItemPlayer(itemId:Int,playerId:Int,amount:Int = 1): ItemUser ={
        db.withConnection { implicit c =>
            val id:Option[Long] = SQL(
                s"""
             INSERT INTO users_items
             (user_id,item_base,amount)
              VALUES ($playerId,$itemId,$amount)
            """).executeInsert()

            new ItemUser(id.get.toInt, itemId, "backpack", amount)
        }
    }
}
