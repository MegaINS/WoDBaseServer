package ru.megains.wod.network.handler

import anorm.SQL
import ru.megains.wod.db.Database
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.packet.login.{CPacketLoginStart, SPacketLoginSuccess}
import ru.megains.wod.{Parsers, WoDServer}

class NetHandlerLoginServer(server: WoDServer, networkManager: NetworkManager) extends INetHandlerLoginServer with Database {

    //var name: String = _

    override def processLoginStart(packetIn: CPacketLoginStart): Unit = {
        val playerEmail:String =  packetIn.email
        val playerPassword:String = packetIn.pass
        withConnection(implicit c=>
            SQL(s"SELECT * FROM player_auth WHERE email='$playerEmail'").as(Parsers.playerAuth.singleOpt).getOrElse(default = (0,"","","")) match {
                case (id,name,mail,password) =>
                    if (playerEmail == mail && playerPassword == password){
                        networkManager.sendPacket(new SPacketLoginSuccess())
                        server.playerList.initializeConnectionToPlayer(networkManager, id,name)
                    }else{
                        println(s"Not player $playerEmail")
                    }
            }

        )
    }


    override def onDisconnect(msg: String): Unit = {

    }

    override def disconnect(msg: String): Unit = {

    }
}


