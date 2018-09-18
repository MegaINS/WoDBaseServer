package ru.megains.wod.inventory

import anorm.SQL
import ru.megains.wod.db.WoDDatabase
import ru.megains.wod.entity.player.Player
import ru.megains.wod.item.{ItemBase, ItemUser, Items}
import ru.megains.wod.network.packet.Status
import ru.megains.wod.network.packet.play.{SPacketActionReturn, SPacketInvUpdate}
import ru.megains.wod.{Action, Parsers}

import scala.collection.mutable

class InventoryBackpack(player: Player)  {



    val db = WoDDatabase.db
    val items = new mutable.HashMap[Int,ItemUser]()
    val size:Int = 0
    val sizeMax:Int = 10000
    def load(): Unit ={
        db.withConnection(implicit c=>{

            val itemsBackpackId: Array[Int] = SQL(
                s"""
                SELECT items
                FROM users_backpack
                WHERE id='${player.id}'
            """).as(Parsers.itemsBackpackId.single)

            val itemsUser: List[ItemUser] = SQL(
                s"""
                SELECT *
                FROM users_items
                WHERE id IN ${itemsBackpackId.mkString("(",",",")")}

            """).as(Parsers.itemUser *)
            
            itemsUser.foreach(item=>{
                items += item.id -> item
            })
        })

    }


    def containsItemBase(id: Int): Boolean = {
        items.values.exists(_.itemBase.id == id)
    }




    def getItemBaseFromId(id: Int, amount: Int = 1):ItemBase = {
        items(id).itemBase
    }

    def getItemFromId(id: Int, amount: Int = 1):ItemUser = {
        val item =  items(id)
        if(item.amount <= amount){
            items -= id
            updateBD()
            item
        }else{
            item.amount -= amount
            db.withConnection(implicit c => {
                SQL(s"""
                        UPDATE users_items
                        SET amount=${item.amount}
                        WHERE id='${item.id}'
                    """).executeUpdate()
            })
            player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),1))
            Items.createItemPlayer(item.itemBase.id,player.id,amount)
        }
    }


    def addItem(item: ItemUser):Boolean = {
        if (item.itemBase.stack) {
            if (containsItemBase(item.itemBase.id)) {
                val itemBackpack = items.values.find(_.itemBase.id == item.itemBase.id).get
                itemBackpack.amount += item.amount
                db.withConnection(implicit c => {
                    SQL(s"""
                        UPDATE users_items
                        SET amount=${itemBackpack.amount}
                        WHERE id='${itemBackpack.id}'
                    """).executeUpdate()
                })
                db.withConnection(implicit c=> {
                    SQL(s"DELETE FROM users_items WHERE id='${item.id}'").execute()
                })
                player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(itemBackpack),1))
                return true
            }
        }
        if (size + 1 <= sizeMax) {
            items += item.id -> item
            player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),0))
            updateBD()
            return true
        }

        false
    }



    def addItemBase(itemBase:ItemBase,amount:Int = 1): Boolean ={
        var result = false
        if (itemBase.stack) {
            if (containsItemBase(itemBase.id)) {
                val itemBackpack = items.values.find(_.itemBase.id == itemBase.id).get
                itemBackpack.amount += amount
                db.withConnection(implicit c => {
                    SQL(
                        s"""
                                UPDATE users_items
                                SET amount=${itemBackpack.amount}
                                WHERE id='${itemBackpack.id}'
                            """).executeUpdate()
                })
                player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(itemBackpack),1))
                result = true
            } else {
                if (size + 1 <= sizeMax) {
                    val item: ItemUser = Items.createItemPlayer(itemBase.id, player.id, amount)
                    items += item.id -> item
                    player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),0))
                    updateBD()
                    result = true
                }
            }
        } else {
            if (size + amount <= sizeMax) {
                val itemsSend:Array[ItemUser] = new Array(amount)
                for (i <- 0 until amount) {
                    val item: ItemUser = Items.createItemPlayer(itemBase.id, player.id)
                    itemsSend(i) = item
                    items += item.id -> item
                }
                player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,itemsSend,0))
                updateBD()
                result = true
            }
        }
        result
    }


    def deleteItem(id: Int,amount:Int): Unit = {
        items -= id
        db.withConnection(implicit c=> {
            SQL(s"DELETE FROM users_items WHERE id='$id'").execute()
        })
        player.sendPacket(new SPacketActionReturn(Status.success, Action.delete,id))
        updateBD()
    }


    def updateBD(): Unit ={
        db.withConnection(implicit c => {
            SQL(s"""
                UPDATE users_backpack
                SET items='${items.keySet.mkString("_")}'
                WHERE id='${player.id}'
             """).executeUpdate()
        })
    }

}
