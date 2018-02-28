package ru.megains.wod.network.handler

import ru.megains.wod.{Logger, WoDServer}
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.packet.CHandshake
import ru.megains.wod.network.packet.ConnectionState.{BATTLE_SERVER, LOGIN, STATUS}

class NetHandlerHandshakeTCP(server: WoDServer, networkManager: NetworkManager) extends INetHandlerHandshakeServer with Logger[NetHandlerHandshakeTCP]{


    override def processHandshake(packetIn: CHandshake): Unit = {

        packetIn.connectionState match {
            case LOGIN =>
                networkManager.setConnectionState(LOGIN)
                networkManager.setNetHandler(new NetHandlerLoginServer(server, networkManager))

            case STATUS =>
                networkManager.setConnectionState(STATUS)

            case BATTLE_SERVER =>
                networkManager.setConnectionState(BATTLE_SERVER)
                networkManager.setNetHandler(new NetHandlerBattleServer(server, networkManager))
                server.battleServer = networkManager
                log.info("Connect Battle server")
            case _ =>
                throw new UnsupportedOperationException("Invalid intention " + packetIn.connectionState)
        }
    }

    override def onDisconnect(msg: String): Unit = {

    }

    override def disconnect(msg: String): Unit = {

    }
}
