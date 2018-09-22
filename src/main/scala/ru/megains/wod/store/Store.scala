package ru.megains.wod.store

import ru.megains.wod.Logger
import ru.megains.wod.entity.player.Player
import ru.megains.wod.item.ItemBase
import ru.megains.wod.network.packet.PacketBuffer

import scala.collection.mutable

class Store(  val id:Int, val name:String) extends Logger[Store]{

    val sections = new mutable.HashMap[Int,StoreTab]()





    def write(buf: PacketBuffer): Unit = {
        buf.writeStringToBuffer(name)
        buf.writeByte(id)
        buf.writeByte(sections.size)
        sections.values.foreach{
            section =>
                buf.writeStringToBuffer(section.name)
                buf.writeByte(section.items.length)
                section.items.foreach(item => buf.writeItemBase(item))
        }
    }

    def buyItem(id: Int, value: Int, player: Player): Unit = {

        var itemBuy:ItemBase = null
        sections.values.foreach{
            section =>  section.items.find(_.id == id).foreach(i=>itemBuy =i)
        }
        player.backpack.addItemBase(itemBuy)
        //TODO money
    }
}
