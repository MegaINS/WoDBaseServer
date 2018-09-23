package ru.megains.wod.db

import anorm.SQL
import ru.megains.wod.Parsers
import ru.megains.wod.entity.EntityStat
import ru.megains.wod.entity.player.{Player, PlayerStat}

object DBPlayerStat extends Database {


    def load(player:Player): PlayerStat ={
        val playerStat = new PlayerStat(player)
        withConnection(implicit c=>{


            val stats = SQL(s"SELECT * FROM player_stat WHERE player_id='${player.id}' ").as(Parsers.playerStat *)
            stats.foreach{
                case(stat,value)=>
                    stat match {
                        case EntityStat.hp=>
                            playerStat.hp = value
                        case EntityStat.hpMax=>
                            playerStat.hpMax = value
                        case EntityStat.pow=>
                            playerStat.power = value
                        case EntityStat.minDam=>
                            playerStat.damageMin = value
                        case EntityStat.maxDam=>
                            playerStat.damageMax = value
                    }

            }
        })
        playerStat
    }
}
