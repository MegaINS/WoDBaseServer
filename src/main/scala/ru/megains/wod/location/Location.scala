package ru.megains.wod.location

import ru.megains.wod.caseclass.LocInfo
import ru.megains.wod.entity.mob.Mob
import ru.megains.wod.entity.player.Player
import ru.megains.wod.network.packet.PacketBuffer
import ru.megains.wod.network.packet.play.{SPacketPlayerLocation, SPacketStore}
import ru.megains.wod.store.Store
import ru.megains.wod.{Action, Logger}

import scala.collection.mutable

class Location(info: LocInfo) extends Logger[Location]{



    val id: Int = info.id
    val name: String = info.name

    val transits = new mutable.HashMap[Int,Location]()
    val players = new mutable.HashMap[Int,Player]()
    val stores = new mutable.HashMap[Int,Store]()
    val mobs = new mutable.HashMap[Int,Mob]()

    def getStore(id: Int): Store = {
        stores.getOrElse(id,default = null)
    }



    def init(): Unit = {

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
        player.sendPacket(new SPacketPlayerLocation(Action.loadPlayerLoc,players.values.toList))
        players += player.id -> player
        players.values.foreach(_.sendPacket(new SPacketPlayerLocation(Action.moveToLcc,List(player))))
    }

    def exit(player: Player): Unit = {
        players -= player.id
        players.values.foreach(_.sendPacket(new SPacketPlayerLocation(Action.exitInLoc,List(player))))
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
