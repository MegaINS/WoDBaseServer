package ru.megains.wod.entity.player

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.entity.db.{Database, WoDDatabase}
import ru.megains.wod.item.ItemUser
import ru.megains.wod.entity.player.BodySlot.BodySlot

import scala.collection.mutable

class PlayerBody(player: Player) {



    val db: Database = WoDDatabase.db

    val slots = new mutable.HashMap[BodySlot,ItemUser]()

    def load(): Unit ={
        db.withConnection(implicit c=>{
            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM users_items
                WHERE user_id='${player.id}'
                AND place='body'
            """).as(Parsers.itemUser *)
            val bodyId = SQL(s"SELECT * FROM users_body WHERE id='${player.id}'").as(Parsers.body.single)
           bodyId.foreach{
               case (slot,id)=>
                   slots += slot -> itemsUser.find(_.id == id).getOrElse(default = null)
           }
        })
    }

    def getItemInSlot(slot: BodySlot): ItemUser = {
        slots.getOrElse(slot,default = null)
    }
    def setSlot(slot: BodySlot, value: ItemUser): Unit = {
        slots(slot) = value
        val id = if(value ne null) value.id else 0

        db.withConnection(implicit c =>
            SQL(s"UPDATE users_body SET ${slot.toString}='$id' WHERE id='${player.id}'").execute()
        )
    }
}
