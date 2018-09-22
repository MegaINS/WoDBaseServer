package ru.megains.wod

import anorm.SQL
import ru.megains.wod.db.Database
import ru.megains.wod.entity.player.{Player, PlayerInfo}
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.handler.NetHandlerPlayServer

import scala.collection.mutable

class PlayerList(server: WoDServer) extends Logger[PlayerList] with Database{

    val players = new mutable.HashMap[Int,Player]()


    def initializeConnectionToPlayer(networkManager: NetworkManager, id: Int,name:String): Unit = {
        log.info(s"Start initialize player id=$id")
        var player:Player = null
        if(players.contains(id)){
            player = players(id)

        }else{
            withConnection(implicit c =>{
                log.info(s"Load player id=$id")
                val playerInfo: PlayerInfo = SQL(s"SELECT * FROM player_info WHERE id='$id'").as(Parsers.playerInfo.single)
                player = new Player(id,name)
                player.load(playerInfo)
                players += player.id -> player
            })
        }
        log.info(s"End initialize player id=$id name=${player.name}")
        val netHandler: NetHandlerPlayServer = new NetHandlerPlayServer(server, networkManager, player)
        player.sendData()
    }


}
