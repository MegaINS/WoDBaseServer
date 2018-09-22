package ru.megains.wod.location

import ru.megains.wod.Logger
import ru.megains.wod.db.DBLocation
import ru.megains.wod.entity.mob.Mobs
import ru.megains.wod.store.{StoreNone, Stores}

object Locations extends Logger[Locations] {


    private val locationsMap = DBLocation.load()


    def getLocation(id:Int): Location ={
        locationsMap.getOrElse(id,default = LocationNone)
    }


    def load(): Unit ={



            val loc_objects = DBLocation.loadObjects()

            for(loc_object<-loc_objects){
                locationsMap.get(loc_object._2) match {
                    case Some(loc1) =>
                        loc_object._3 match {
                            case "loc" =>
                                locationsMap.get(loc_object._4)match {
                                    case Some(loc2) =>
                                        loc1.transits += loc2.id ->loc2
                                    case None =>
                                        log.info(s"Error init location 1 transit id=${loc_object._2} ")
                                }
                            case "store"=>
                              Stores.getStore(loc_object._4) match {
                                  case StoreNone =>
                                      log.info(s"Error init Store 1 transit id=${loc_object._2} ")
                                  case store  =>
                                      loc1.stores += store.id -> store
                              }
                            case "mob"=>
                                Mobs.getMob(loc_object._4) match {
                                    case Some(mob) =>
                                        loc1.mobs += mob.id ->mob
                                    case None =>
                                        log.info(s"Error init Mobs 1 transit id=${loc_object._2} ")
                                }
                            case _ =>
                                log.info(s"Error init loc_object ${loc_object._1} type = ${loc_object._3}")
                        }
                    case None =>
                        log.info(s"Error init location 2 transit id=${loc_object._4} ")
                }

            }

    }
}
class Locations{

}