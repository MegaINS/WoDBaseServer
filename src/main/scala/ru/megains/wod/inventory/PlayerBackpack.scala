package ru.megains.wod.inventory

import ru.megains.wod.Action
import ru.megains.wod.db.DBPlayerItem
import ru.megains.wod.entity.player.Player
import ru.megains.wod.item.{ItemBase, ItemUser, Items}
import ru.megains.wod.network.packet.Status
import ru.megains.wod.network.packet.play.{SPacketActionReturn, SPacketInvUpdate}

import scala.collection.mutable

class PlayerBackpack(player: Player)  {



    val items: mutable.HashMap[Int, ItemUser] = DBPlayerItem.loadInventory(player.id,"backpack")
    val size:Int = 0
    val sizeMax:Int = 10000

    def containsItemBase(id: Int): Boolean = {
        items.values.exists(_.itemBase.id == id)
    }


    def getItem(id: Int): Option[ItemUser] = {
        items.get(id)
    }


    def getItemBaseFromId(id: Int, amount: Int = 1):ItemBase = {
        items(id).itemBase
    }

    def getItemFromId(id: Int, amount: Int = 1):ItemUser = {
        val item =  items(id)
        if(item.amount <= amount){
            items -= id
            item
        }else{
            item.amount -= amount
            DBPlayerItem.updateAmount(item.id,item.amount)
            player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),1))
            Items.createItemPlayer(item.itemBase.id,player.id,amount)
        }
    }


    def removeItem(id: Int): Unit = {
        items.get(id) match {
            case Some(item) =>
                items -= id
                player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),10))
            case _ =>
                println("Missing item in backpack id ="+id)
        }
    }

    def getItemFromBaseId(id:Int): Option[ItemUser] ={
        items.values.find(_.itemBase.id == id)
    }



    def addItem(item: ItemUser):Boolean = {
        if(item.itemBase.stack){
            getItemFromBaseId(item.itemBase.id) match {
                case Some(itemBackpack) =>
                    itemBackpack.amount += item.amount
                    DBPlayerItem.updateAmount(itemBackpack.id,itemBackpack.amount)
                    DBPlayerItem.deleteItem(item.id)
                    player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(itemBackpack),1))
                    true
                case None =>
                    items += item.id -> item
                    DBPlayerItem.updateInventory(item.id,"backpack")
                    player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),0))
                    true
            }

        }else{
            items += item.id -> item
            DBPlayerItem.updateInventory(item.id,"backpack")
            player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),0))
            true
        }

    }
//    def addItem(item: ItemUser):Boolean = {
//        if (item.itemBase.stack) {
//            if (containsItemBase(item.itemBase.id)) {
//                val itemBackpack = items.values.find(_.itemBase.id == item.itemBase.id).get
//                itemBackpack.amount += item.amount
//                withConnection(implicit c => {
//                    SQL(s"""
//                        UPDATE player_item
//                        SET amount=${itemBackpack.amount}
//                        WHERE id='${itemBackpack.id}'
//                    """).executeUpdate()
//                })
//                withConnection(implicit c=> {
//                    SQL(s"DELETE FROM player_item WHERE id='${item.id}'").execute()
//                })
//                player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(itemBackpack),1))
//                return true
//            }
//        }
//        if (size + 1 <= sizeMax) {
//            items += item.id -> item
//            player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),0))
//            return true
//        }
//
//        false
//    }



    def addItemBase(itemBase:ItemBase,amount:Int = 1): Boolean ={
        var result = false
        if (itemBase.stack) {
            if (containsItemBase(itemBase.id)) {
                val itemBackpack = items.values.find(_.itemBase.id == itemBase.id).get
                itemBackpack.amount += amount
                DBPlayerItem.updateAmount(itemBackpack.id,itemBackpack.amount)
                player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(itemBackpack),1))
                result = true
            } else {
                if (size + 1 <= sizeMax) {
                    val item: ItemUser = Items.createItemPlayer(itemBase.id, player.id, amount)
                    items += item.id -> item
                    player.sendPacket(new SPacketInvUpdate(InventoryType.backpack,Array(item),0))
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
                result = true
            }
        }
        result
    }


    def deleteItem(id: Int,amount:Int): Unit = {
        items -= id
        DBPlayerItem.deleteItem(id)
        player.sendPacket(new SPacketActionReturn(Status.success, Action.delete,id))
    }


}
