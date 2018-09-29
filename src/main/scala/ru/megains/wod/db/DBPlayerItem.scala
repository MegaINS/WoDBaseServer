package ru.megains.wod.db

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.item.ItemUser

import scala.collection.mutable

object DBPlayerItem extends Database {



    def loadInventory(playerId:Int,inventory:String): mutable.HashMap[Int,ItemUser] ={

        val items = new mutable.HashMap[Int,ItemUser]()
        withConnection(implicit c=>{
            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM player_item
                WHERE player_id=$playerId AND inventory='$inventory'

            """).as(Parsers.itemUser *)

            itemsUser.foreach(item=>{
                items += item.id -> item
            })
        })
        items
    }






    def updateAmount(id: Int, amount: Int): Int = {
        withConnection(implicit c => {
            SQL(s"""
                        UPDATE player_item
                        SET amount=$amount
                        WHERE id='$id'
                    """).executeUpdate()
        })
    }


    def updateInventory(itemId: Int, inventory: String): Boolean = {
        withConnection(implicit c =>
            SQL(s"UPDATE player_item SET inventory='$inventory' WHERE id='$itemId'").execute()
        )
    }

    def deleteItem(id: Int): Boolean = {
        withConnection(implicit c=> {
            SQL(s"DELETE FROM player_item WHERE id='$id'").execute()
        })
    }
}
