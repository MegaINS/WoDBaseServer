package ru.megains.wod.battle

import ru.megains.wod.entity.Entity

import scala.collection.mutable

object BattleList {

    val battleMap = new mutable.HashMap[Int,Battle]()


    def createBattle(entity1:Entity,entity2:Entity): Int ={

        val battle = new Battle(battleMap.size,0)
        battleMap += battle.id ->battle
        battle.addEntity(entity1,1)
        battle.addEntity(entity2,2)
        battle.id
    }

    def get(id:Int): Battle ={
        battleMap(id)
    }
}
