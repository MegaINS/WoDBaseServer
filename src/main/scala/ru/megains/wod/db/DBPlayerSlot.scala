package ru.megains.wod.db

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.item.ItemUser

object DBPlayerSlot extends Database {


    def loadOpenSlot(playerId:Int): Int ={
        var openSlot:Int = 0
        withConnection(implicit c=>{
            SQL(
                s"""
                SELECT *
                FROM player_slot
                WHERE id='$playerId'
            """).as(Parsers.playerSlot.single).foreach(slo => if(slo != -1)openSlot+=1)
        })
        openSlot
    }

    def load(playerId:Int): Array[ItemUser] = {
        val slotsItem:Array[ItemUser] = new Array[ItemUser](10)
        withConnection(implicit c=>{

            val itemsSlot: Array[Int] = SQL(
                s"""
                SELECT *
                FROM player_slot
                WHERE id='$playerId'
            """).as(Parsers.playerSlot.single)

            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM player_item
                WHERE id IN ${itemsSlot.mkString("(",",",")")}
            """).as(Parsers.itemUser *)


            for (i <- itemsSlot.indices){
                slotsItem(i) = itemsUser.find(_.id == itemsSlot(i)).getOrElse(default = null)
            }

        })
        slotsItem
    }

    def update(playerId:Int, slotId:Int,itemId:Int): Unit ={
        withConnection(implicit c=> {
            SQL(s"""
                    UPDATE player_slot
                    SET slot_$slotId=$itemId
                    WHERE id='$playerId'
                """).executeUpdate()
        })
    }

}
