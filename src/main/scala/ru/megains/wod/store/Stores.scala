package ru.megains.wod.store

import ru.megains.wod.Logger
import ru.megains.wod.db.DBStore
import ru.megains.wod.item.Items

import scala.collection.mutable

object Stores extends Logger[Stores]{

    private val storesMap = new mutable.HashMap[Int,Store]()
    private val storeSectionsMap = new mutable.HashMap[Int,StoreTab]()


    def getStore(id:Int): Store ={
        storesMap.getOrElse(id,default = StoreNone)
    }
    def getStoreSection(id:Int): StoreTab ={
        storeSectionsMap.getOrElse(id,default = new StoreTab(-1,""))
    }

    def load(): Unit ={


            val storeSections =  DBStore.loadTab()
            for(storeSection<-storeSections){
                storeSectionsMap += storeSection.id -> storeSection
            }

            val storeTabItemBase = DBStore.loadTabIdItemId()
            for(ti<-storeTabItemBase){
                storeSectionsMap(ti._1).items += Items.getItem(ti._2)
            }

            val stores = DBStore.loadStore()
            for(store<-stores){
                storesMap += store.id -> store
            }

            val storeStoreTab = DBStore.loadStoreIdTabId()
            for(ss<-storeStoreTab){
                val section = getStoreSection(ss._2)
                if(section.id == -1 ){
                    log.info(s"Error init location ${ss._1} ${storesMap(ss._1).name} transits id=${ss._2} ")
                }else{
                    storesMap(ss._1).sections +=  section.id -> section
                }

            }
    }

}
class Stores


