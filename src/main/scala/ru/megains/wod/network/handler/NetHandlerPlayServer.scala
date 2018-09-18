package ru.megains.wod.network.handler

import ru.megains.wod.battle.BattleList
import ru.megains.wod.entity.EntityType
import ru.megains.wod.entity.mob.Mobs
import ru.megains.wod.entity.player.{SlotType, Player}
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.packet.Packet
import ru.megains.wod.network.packet.battle.PCreateBattle
import ru.megains.wod.network.packet.play.CPacketAction
import ru.megains.wod.{Action, Logger, WoDServer}

class NetHandlerPlayServer(server: WoDServer, val networkManager: NetworkManager, player: Player) extends INetHandlerPlayServer with Logger[NetHandlerPlayServer] {

    networkManager.setNetHandler(this)

    player.setConnection(this)


    override def sendPacket(packetIn: Packet[_ <: INetHandler]) {

        try
            networkManager.sendPacket(packetIn)

        catch {
            case throwable: Throwable =>
                log.error("sendPacket", throwable)
        }
    }


    def disconnect(msg: String): Unit ={
        networkManager.disconnect(msg)
    }



    def processAction(packet: CPacketAction): Unit={
        val id = packet.id
        packet.action match {
            case Action.moveToLcc =>
                player.location().moveToLocation(id,player)
            case Action.takeOff =>
                player.takeOff(SlotType(id),packet.value1)
            case Action.take =>
                player.take(id)
            case Action.delete =>
                player.backpack.deleteItem(id,-1000)
            case Action.store =>
                player.location().sendStore(id,player)
            case Action.storeBuy =>
                player.location().getStore(id).buyItem(packet.value1,packet.value2,player)
            case Action.attack =>

                val battleId = BattleList.createBattle(player,Mobs.getMob(packet.id))

                server.battleServer.sendPacket(new PCreateBattle(battleId, player.id,EntityType.player,packet.id,EntityType.mob))


            case _ => log.info(s"Error Action ${ packet.action}")
        }
    }



    override def onDisconnect(msg: String): Unit = {

        log.info("{} lost connection: {}", Array[AnyRef](player.name, msg))

    }







}
