package ru.megains.wod.entity.mob

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.entity.db.WoDDatabase

import scala.collection.mutable

object Mobs {

    val db = WoDDatabase.db
    private val mobsMap = new mutable.HashMap[Int,Mob]()


    def getMob(id:Int): Mob ={
        mobsMap.getOrElse(id,default = null)
    }


    def load(): Unit ={
        db.withConnection(implicit c=>{
            val mobs = SQL(s"SELECT * FROM mob ").as(Parsers.mob *)
            for(mob<-mobs){
                mobsMap += mob.id -> mob
            }
        })
    }
}
