package ru.megains.wod.item

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.db.Database
import ru.megains.wod.item.ItemParam.ItemParam

import scala.collection.mutable

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
    def loadParams(id: Int): mutable.HashMap[ItemParam, Int] = {
        val itemParams: mutable.HashMap[ItemParam,Int] = new mutable.HashMap[ItemParam,Int]()
        withConnection { implicit c =>
            val itemParam = SQL(s"SELECT * FROM item_param WHERE item_id='$id'").as(Parsers.itemParam *)
            itemParam.foreach{
                case (param,value)=>
                    itemParams +=  param -> value

            }
        }
        itemParams
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
