package ru.megains.wod.item

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.db.Database

object Items extends Database{


    var idItems = Map.empty[Int,ItemBase]


    def  getItem(id:Int): ItemBase ={
        if(id < 1){
            null
        }else if(idItems.contains(id)){
            idItems(id)
        }else{
            withConnection { implicit c =>
                val item = SQL(s"SELECT * FROM item_base WHERE id='$id'").as(Parsers.itemBase.single)
                idItems += id -> item
                item
            }
        }
    }

    def createItemPlayer(itemId:Int,playerId:Int,amount:Int = 1): ItemUser ={
        withConnection { implicit c =>
            val id:Option[Long] = SQL(
                s"""
                    INSERT INTO player_item
                    (player_id,item_id,amount)
                    VALUES ($playerId,$itemId,$amount)
                """).executeInsert()

            new ItemUser(id.get.toInt, itemId, amount)
        }
    }
}
