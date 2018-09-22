package ru.megains.wod.db

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.item.ItemUser

import scala.collection.mutable

object DBPlayerBody extends Database {


    def load(playerId:Int): mutable.HashMap[SlotType,ItemUser] ={
        val slots = new mutable.HashMap[SlotType,ItemUser]()
        withConnection(implicit c=>{

            val bodyId = SQL(s"SELECT * FROM player_body WHERE id='$playerId'").as(Parsers.body.single)

            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM player_item
                WHERE id IN ${bodyId.values.mkString("(",",",")")}
            """).as(Parsers.itemUser *)

            bodyId.foreach{
                case (slot,id)=>
                    slots += slot -> itemsUser.find(_.id == id).getOrElse(default = null)
            }
        })
        slots
    }

    def updateSlot(playerId: Int, slot: SlotType, itemId: Int): Boolean = {
        withConnection(implicit c =>
            SQL(s"UPDATE player_body SET ${slot.toString}='$itemId' WHERE id='$playerId'").execute()
        )
    }

}
