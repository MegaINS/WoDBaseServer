package ru.megains.wod.entity.player

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.db.WoDDatabase
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.play.SPacketSlotUpdate

class PlayerSlots(val player: Player) {


    val db = WoDDatabase.db
    var openSlots:Int = 0
    var slotsItem:Array[ItemUser] = new Array[ItemUser](10)


    def load(): Unit = {
        db.withConnection(implicit c=>{

            val itemsSlot: Array[Int] = SQL(
                s"""
                SELECT *
                FROM users_slot
                WHERE id='${player.id}'
            """).as(Parsers.userSlot.single)

            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM users_items
                WHERE id IN ${itemsSlot.mkString("(",",",")")}
            """).as(Parsers.itemUser *)

            openSlots = itemsSlot.count(_ > -1)

            for (i <- itemsSlot.indices){
                slotsItem(i) = itemsUser.find(_.id == itemsSlot(i)).getOrElse(default = null)
            }

        })
    }

    def take(id:Int): Unit = {
        for(i <- 0 until openSlots ){
            if(slotsItem(i) == null){
                val value = 1
                val itemSlot = player.backpack.getItemFromId(id,value)
                slotsItem(i) = itemSlot

                db.withConnection(implicit c=> {
                    SQL(s"""
                    UPDATE users_slot
                    SET item$i=${itemSlot.id}
                    WHERE id='${player.id}'
                """).executeUpdate()
                })
              //  player.sendPacket(new SPacketActionReturn(Status.success,Action.take,item.id))
                player.sendPacket(new SPacketSlotUpdate(i,itemSlot))
                return
            }
        }
    }

    def takeOff(id:Int): Unit = {
        for(i <- 0 until openSlots ){
            if(slotsItem(i) != null &&slotsItem(i).id == id){

                val item = slotsItem(i)
                slotsItem(i) = null
                db.withConnection(implicit c=> {
                    SQL(s"""
                    UPDATE users_slot
                    SET item$i=0
                    WHERE id='${player.id}'
                """).executeUpdate()
                })
                player.sendPacket(new SPacketSlotUpdate(i))

                player.backpack.addItem(item)
                //TODO false


            }
        }

    }
}
