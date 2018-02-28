package ru.megains.wod.network.handler

import ru.megains.wod.battle.BattleList
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.packet.Packet
import ru.megains.wod.network.packet.battle.PBattleStatus
import ru.megains.wod.{Logger, WoDServer}

class NetHandlerBattleServer(server: WoDServer, networkManager: NetworkManager) extends INetHandler with Logger[NetHandlerBattleServer]{




    def processBattleStatus(packet: PBattleStatus): Unit = {

        packet.status match {
            case 1 =>
               val battle = BattleList.get(packet.id)
                battle.status = 1
                battle.sendAllStart()

            case 2 =>

            case _ =>  log.info(s"Error Battle Status ${packet.status}")
        }


    }






    override def sendPacket(packetIn: Packet[_ <: INetHandler]) {

        try
            networkManager.sendPacket(packetIn)

        catch {
            case throwable: Throwable =>
                log.error("sendPacket", throwable)
        }
    }

    override def onDisconnect(msg: String): Unit = {
        log.info("{} lost connection: {}", Array[AnyRef]("Battle Server", msg))
    }

    override def disconnect(msg: String): Unit = {
        log.info("{} lost connection: {}", Array[AnyRef]("Battle Server", msg))
    }
}
