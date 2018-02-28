package ru.megains.wod.inventory

import anorm.SQL
import ru.megains.wod.{Action, Parsers}
import ru.megains.wod.item.ItemUser
import ru.megains.wod.network.packet.Status
import ru.megains.wod.network.packet.play.{SPacketActionReturn, SPacketInvUpdate}
import ru.megains.wod.entity.player.Player

class InventoryBackpack(player: Player) extends Inventory{



    def load(): Unit ={
        db.withConnection(implicit c=>{
            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM users_items
                WHERE user_id='${player.id}'
                AND place='backpack'
            """).as(Parsers.itemUser *)
            
            itemsUser.foreach(item=>{
                items += item.id -> item
            })
        })

    }

    def addItems(itemsIn: Array[ItemUser]): Unit = {
        itemsIn.foreach(item=>{
            items += item.id -> item
        })
        player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,itemsIn,0))
    }

    def contains(id: Int): Boolean = {
        items.values.exists(_.itemBase.id == id)
    }

    def updateItem(id: Int, value: Int) = {
       val item =  items.values.find(_.itemBase.id == id).get
        item.amount += value
        db.withConnection(implicit c=> {
            SQL(s"""
                    UPDATE users_items
                    SET amount=${item.amount}
                    WHERE id='${item.id}'
                """).executeUpdate()
        })
        player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),1))
    }

    def delete(id: Int): Unit = {
        items -= id
        db.withConnection(implicit c=> {
            SQL(s"DELETE FROM users_items WHERE id='$id'").execute()
        })
        player.sendPacket(new SPacketActionReturn(Status.success, Action.delete,id))
    }

}
