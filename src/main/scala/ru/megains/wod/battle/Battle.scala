package ru.megains.wod.battle

import ru.megains.wod.entity.Entity
import ru.megains.wod.network.packet.play.SPacketStartBattle

import scala.collection.mutable.ArrayBuffer

class Battle(val id:Int, var status:Int) {


    /*
    * status
    * 0 await
    * 1 active
    * 2 end
    * */


    val teamEntity1 = new ArrayBuffer[Entity]()
    val teamEntity2 = new ArrayBuffer[Entity]()
    def addEntity(entity: Entity,team:Int): Unit ={
        team match {
            case 1 =>teamEntity1 += entity
            case 2 =>teamEntity2 += entity
            case _ => println(s"Error team $team")
        }
    }

    def sendAllStart(): Unit = {
        teamEntity1.foreach(e=>e.sendPacket(new SPacketStartBattle(id)))
        teamEntity2.foreach(e=>e.sendPacket(new SPacketStartBattle(id)))
    }


}
