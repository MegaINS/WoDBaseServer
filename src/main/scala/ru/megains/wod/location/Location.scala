package ru.megains.wod.location

import ru.megains.wod.Logger
import ru.megains.wod.caseclass.LocInfo
import ru.megains.wod.entity.mob.{Mob, Mobs}
import ru.megains.wod.network.packet.PacketBuffer
import ru.megains.wod.network.packet.play.SPacketStore
import ru.megains.wod.entity.player.Player
import ru.megains.wod.store.{Store, StoreNone, Stores}

import scala.collection.mutable

class Location(info: LocInfo) extends Logger[Location]{



    val id = info.id
    val name = info.name

    private val transits = new mutable.HashMap[Int,Location]()
    private val players = new mutable.HashMap[Int,Player]()
    private val stores = new mutable.HashMap[Int,Store]()
    private val mobs = new mutable.HashMap[Int,Mob]()

    def getStore(id: Int): Store = {
        stores.getOrElse(id,default = null)
    }



    def init(): Unit = {
        info.transits.foreach(id =>{
            val loc = Locations.getLocation(id)
            if(loc == LocationNone){
                log.info(s"Error init location ${info.id} ${info.name} transit id=$id ")
            }else{
                transits += id -> loc
            }
        })
        info.stores.foreach(id =>{
            val store = Stores.getStore(id)
            if(store == StoreNone){
                log.info(s"Error init location ${info.id} ${info.name} store id=$id ")
            }else{
                stores += id -> store
            }
        })
        info.mobs.foreach(id =>{
            val mob = Mobs.getMob(id)
            if(mob == null){
                log.info(s"Error init location ${info.id} ${info.name} mob id=$id ")
            }else{
                mobs += id -> mob
            }
        })
    }

    def moveToLocation(id: Int,player: Player): Unit = {
        transits.get(id).foreach{
            case loc:Location =>
                exit(player)
                loc.enter(player)
            case _ => log.info(s"Error moveToLocation name = $name to id = $id ")
        }
    }

    def sendStore(id: Int, player: Player): Unit = {
        if(stores.contains(id)){
            player.sendPacket(new SPacketStore(stores(id)))
        }else{
            log.info(s"Error sendStore name = $name to id = $id ")
        }
    }

    def enter(player: Player): Unit ={
        player.location(this)
        players += player.id -> player
    }
    def exit(player: Player): Unit = {
        players -= player.id
    }
    def write(buf: PacketBuffer): Unit ={
        buf.writeStringToBuffer(name)
        buf.writeByte(transits.size)
        transits.foreach{
            case (locId,loc)=>
                buf.writeShort(locId)
                buf.writeStringToBuffer(loc.name)
        }
        buf.writeByte(stores.size)
        stores.foreach{
            case (storeId,store)=>
                buf.writeShort(storeId)
                buf.writeStringToBuffer(store.name)
        }
        buf.writeByte(mobs.size)
        mobs.foreach{
            case (mobId,mob)=>
                buf.writeInt(mobId)
                buf.writeStringToBuffer(mob.name)
                buf.writeShort(mob.level)
        }

    }
}
