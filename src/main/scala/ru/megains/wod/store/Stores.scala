package ru.megains.wod.store

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.entity.db.WoDDatabase

import scala.collection.mutable

object Stores {

    val db = WoDDatabase.db
    private val storesMap = new mutable.HashMap[Int,Store]()
    private val storeSectionsMap = new mutable.HashMap[Int,StoreSection]()


    def getStore(id:Int): Store ={
        storesMap.getOrElse(id,default = StoreNone)
    }
    def getStoreSection(id:Int): StoreSection ={
        storeSectionsMap.getOrElse(id,default = StoreSection(-1,"",Array.empty))
    }

    def load(): Unit ={
        db.withConnection(implicit c=>{

            val storeSections = SQL(" SELECT * FROM store_section ").as(Parsers.storeSection *)
            for(storeSection<-storeSections){
                storeSectionsMap += storeSection.id -> storeSection
            }




            val stores = SQL(s"SELECT * FROM store ").as(Parsers.store *)
            for(store<-stores){
                storesMap += store.id -> new Store(store)
            }
            storesMap.values.foreach(_.init())
        })
    }

}

