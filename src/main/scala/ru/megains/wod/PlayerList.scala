package ru.megains.wod

import anorm.SQL
import ru.megains.wod.entity.db.{Database, WoDDatabase}
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.handler.NetHandlerPlayServer
import ru.megains.wod.entity.player.{Player, PlayerInfo}

import scala.collection.mutable

class PlayerList(server: WoDServer) extends Logger[PlayerList]{

    val db: Database = WoDDatabase.db
    val players = new mutable.HashMap[Int,Player]()


    def initializeConnectionToPlayer(networkManager: NetworkManager, id: Int): Unit = {
        log.info(s"Start initialize player id=$id")
        var player:Player = null
        if(players.contains(id)){
            player = players(id)

        }else{
            db.withConnection(implicit c =>{
                log.info(s"Load player id=$id")
                val userInfo: PlayerInfo = SQL(s"SELECT * FROM users_info WHERE id='$id'").as(Parsers.userInfo.single)
                player = new Player(userInfo.id,userInfo.name)
                player.load(userInfo)
                players += player.id -> player
            })
        }
        log.info(s"End initialize player id=$id name=${player.name}")
        val netHandler: NetHandlerPlayServer = new NetHandlerPlayServer(server, networkManager, player)
        player.sendData()
    }


}
