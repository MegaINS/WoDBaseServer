package ru.megains.wod

import anorm.SqlParser.get
import anorm.{RowParser, ~}
import ru.megains.wod.caseclass.{LocInfo, StoreInfo}
import ru.megains.wod.entity.mob.Mob
import ru.megains.wod.entity.player.SlotType.SlotType
import ru.megains.wod.entity.player.{PlayerInfo, SlotType}
import ru.megains.wod.item._
import ru.megains.wod.store.StoreSection

import scala.collection.immutable.HashMap

object Parsers {
    val loc_loc: RowParser[(Int,Int,Int)] = {
        get[Int]("id") ~
        get[Int]("locid_in") ~
        get[Int]("locid_out") map{
            case id~idIn~idOut =>(id,idIn,idOut)
        }
    }


    val itemBase: RowParser[ItemBase] = {
        get[Int]("id") ~
        get[String]("name") ~
        get[String]("img") ~
        get[Int]("tupe") ~
        get[Int]("level") ~
        get[Int]("cost") ~
        get[Boolean]("weight") ~
        get[Boolean]("privat")~
        get[String]("action")~
        get[String]("slot")~
        get[Boolean]("stack")map{
            case id~name~img~tupe~level~cost~weight~privat~action~slot~stack => new ItemBase(id,name,img,tupe,level,cost,weight,privat,ItemAction.withName(action),SlotType.withName(slot),stack )
        }
    }

    val itemUser: RowParser[ItemUser]={
        get[Int]("id")~
        get[Int]("item_base")~
        get[Int]("amount")map{
            case id~baseId~amount => new ItemUser(id,baseId,amount)
        }
    }


    val itemsBackpackId: RowParser[Array[Int]]={
        get[String]("items") map (items => items.split("_").map((s) => Integer.parseInt(s)))
    }

    val location: RowParser[LocInfo] = {
        get[Int]("id") ~
        get[String]("name") map{
            case id~name =>LocInfo(id,name)
        }
    }

    val body: RowParser[Map[SlotType, Int]] ={

        get[Int]("head") ~
        get[Int]("gloves") ~
        get[Int]("leggings") ~
        get[Int]("boots") ~
        get[Int]("hauberk") ~
        get[Int]("belt") ~
        get[Int]("cuirass") ~
        get[Int]("shoulders")~
        get[Int]("leftHand") ~
        get[Int]("rightHand") map{

            case  head~gloves~leggings~boots~hauberk~belt~cuirass~shoulders~leftHand~rightHand =>

                HashMap[SlotType,Int](
                 SlotType.head -> head,
                 SlotType.gloves ->gloves,
                 SlotType.leggings ->leggings,
                 SlotType.boots ->boots,
                 SlotType.hauberk ->hauberk,
                 SlotType.belt ->belt,
                 SlotType.cuirass ->cuirass,
                 SlotType.shoulders ->shoulders,
                 SlotType.leftHand ->leftHand,
                 SlotType.rightHand ->rightHand
            )
        }


    }

    val storeSection: RowParser[StoreSection] = {
        get[Int]("id")~
        get[String]("name") ~
        get[String]("items")map{
            case id~name~section =>
                val items: Array[ItemBase] =  section.split("_").map(_.toInt).map(id=> Items.getItem(id))
                StoreSection(id,name,items)
        }
    }

    val store: RowParser[StoreInfo] = {
        get[Int]("id")~
        get[String]("name") ~
        get[String]("section")map{
            case id~name~section => StoreInfo(id,name,section.split("_").map(_.toInt))
        }
    }

    val userAuth: RowParser[(Int, String, String)] = {
        get[Int]("id") ~
                get[String]("email") ~
                get[String]("password") map{
            case id~mail~password => (id,mail,password)
        }
    }

    val userInfo: RowParser[PlayerInfo] = {
        get[Int]("id") ~
        get[String]("name") ~
        get[Int]("level") ~
        get[Int]("exp") ~
        get[Int]("location") ~
        get[Int]("money") map{
            case id~name~level~exp~loc~money =>new PlayerInfo(id,name,level,exp,loc,money)
        }
    }

    val userSlot: RowParser[Array[Int]] = {
        get[Int]("item0") ~
        get[Int]("item1") ~
        get[Int]("item2") ~
        get[Int]("item3") ~
        get[Int]("item4") ~
        get[Int]("item5") ~
        get[Int]("item6") ~
        get[Int]("item7") ~
        get[Int]("item8") ~
        get[Int]("item9") map{
            case item0~item1~item2~item3~item4~item5~item6~item7~item8~item9 =>
                Array(item0,item1,item2,item3,item4,item5,item6,item7,item8,item9)
        }
    }

    val mob: RowParser[Mob] = {
        get[Int]("id") ~
        get[String]("name") ~
        get[Int]("level")map{
            case id~name~level => new Mob(id, name, level)
        }
    }






}
