package ru.megains.wod.store

import ru.megains.wod.Logger
import ru.megains.wod.caseclass.StoreInfo
import ru.megains.wod.entity.player.Player
import ru.megains.wod.item.ItemBase
import ru.megains.wod.network.packet.PacketBuffer

import scala.collection.mutable

class Store(storeInfo: StoreInfo) extends Logger[Store]{



    val name = storeInfo.name
    val id = storeInfo.id
    private val sections = new mutable.HashMap[Int,StoreSection]()


    def init(): Unit = {
        storeInfo.storeSection.foreach(id =>{
            val section = Stores.getStoreSection(id)
            if(section.id == -1 ){
                log.info(s"Error init location ${storeInfo.id} ${storeInfo.name} transits id=$id ")
            }else{
                sections += id -> section
            }
        })
    }


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
