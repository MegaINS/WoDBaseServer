package ru.megains.wod.location

import anorm.SQL
import ru.megains.wod.{Logger, Parsers}
import ru.megains.wod.db.WoDDatabase

import scala.collection.mutable

object Locations extends Logger[Locations] {

    val db = WoDDatabase.db
    private val locationsMap = new mutable.HashMap[Int,Location]()


    def getLocation(id:Int): Location ={
        locationsMap.getOrElse(id,default = LocationNone)
    }


    def load(): Unit ={
        db.withConnection(implicit c=>{
            val locations = SQL("SELECT * FROM location ").as(Parsers.location *)
            for(loc<-locations){
                locationsMap += loc.id -> new Location(loc)
            }
            locationsMap.values.foreach(_.init())
            val loc_loc = SQL("SELECT * FROM loc_loc ").as(Parsers.loc_loc *)
            for(loc<-loc_loc){
                locationsMap.get(loc._2) match {
                    case Some(locIn) => locationsMap.get(loc._3)match {
                        case Some(locOut) => locIn.transits += locOut.id ->locOut
                        case None =>
                            log.info(s"Error init location out transit id=${loc._1} ")
                    }
                    case None =>
                        log.info(s"Error init location in transit id=${loc._1} ")
                }
            }


        })
    }
}
class Locations{

}