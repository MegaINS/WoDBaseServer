package ru.megains.wod

import ru.megains.wod.network.{NetworkManager, NetworkSystem}

class WoDServer extends Logger[WoDServer] {

    var battleServer: NetworkManager = _
    var isActive = true
    var playerList:PlayerList = _
    var gameLogicHandler:GameLogicHandler = _
    val networkSystem:NetworkSystem =  new NetworkSystem(this)


    def start(): Unit ={
        log.info("Start WoDServer")
        playerList = new PlayerList(this)
        log.info("Start GameLogicHandler")
        gameLogicHandler = new GameLogicHandler(this)
        log.info("Load mobs")
       // Mobs.load()
        log.info("Load stores")
        //Stores.load()
        log.info("Load locations")
        //Locations.load()

        log.info("Start NetworkSystem")
        networkSystem.startLan(null,8080)
    }

}
