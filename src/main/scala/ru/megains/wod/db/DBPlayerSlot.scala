package ru.megains.wod.db

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemUser

import scala.collection.mutable

object DBPlayerSlot extends Database {


    def loadOpenSlot(playerId:Int): Int ={
        var openSlot:Int = 0
        withConnection(implicit c=>{
            SQL(
                s"""
                SELECT *
                FROM player_slot
                WHERE id='$playerId'
            """).as(Parsers.playerSlot.single).values.foreach(slo => if(slo != -1)openSlot+=1)
        })
        openSlot
    }

    def load(playerId:Int):mutable.HashMap[SlotType, ItemUser]= {
        val slotsItem = new mutable.HashMap[SlotType, ItemUser]
        withConnection(implicit c=>{

            val itemsSlots = SQL(
                s"""
                SELECT *
                FROM player_slot
                WHERE id='$playerId'
            """).as(Parsers.playerSlot.single)

            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM player_item
                WHERE id IN ${itemsSlots.values.mkString("(",",",")")}
            """).as(Parsers.itemUser *)


            for ( itemsSlot<- itemsSlots){
                slotsItem += itemsSlot._1 -> itemsUser.find(_.id == itemsSlot._2).getOrElse(default = null)
            }

        })
        slotsItem
    }

    def update(playerId:Int, slot:String,itemId:Int): Unit ={
        withConnection(implicit c=> {
            SQL(s"""
                    UPDATE player_slot
                    SET $slot=$itemId
                    WHERE id='$playerId'
                """).executeUpdate()
        })
    }

}
