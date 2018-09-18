package ru.megains.wod.location

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.db.WoDDatabase

import scala.collection.mutable

object Locations extends {

    val db = WoDDatabase.db
    private val locationsMap = new mutable.HashMap[Int,Location]()


    def getLocation(id:Int): Location ={
        locationsMap.getOrElse(id,default = LocationNone)
    }


    def load(): Unit ={
        db.withConnection(implicit c=>{
            val locations = SQL(s"SELECT * FROM locations ").as(Parsers.location *)
            for(loc<-locations){
                locationsMap += loc.id -> new Location(loc)
            }
            locationsMap.values.foreach(_.init())
        })
    }
}
