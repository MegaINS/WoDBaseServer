package ru.megains.wod.network.handler

import anorm.SQL
import ru.megains.wod.entity.db.WoDDatabase
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.packet.login.{CPacketLoginStart, SPacketLoginSuccess}
import ru.megains.wod.{Parsers, WoDServer}

class NetHandlerLoginServer(server: WoDServer, networkManager: NetworkManager) extends INetHandlerLoginServer{

    var name: String = _
    val db = WoDDatabase.db

    override def processLoginStart(packetIn: CPacketLoginStart): Unit = {
        val userEmail:String =  packetIn.email
        val userPassword:String = packetIn.pass
        db.withConnection(implicit c=>
            SQL(s"SELECT * FROM users_auth WHERE email='$userEmail'").as(Parsers.userAuth.singleOpt).getOrElse(default = (0,"","")) match {
                case (id,mail,password) =>
                    if (userEmail == mail && userPassword == password){
                        networkManager.sendPacket(new SPacketLoginSuccess())
                        server.playerList.initializeConnectionToPlayer(networkManager, id)
                    }else{

                    }
            }

        )
    }


    override def onDisconnect(msg: String): Unit = {

    }

    override def disconnect(msg: String): Unit = {

    }
}


