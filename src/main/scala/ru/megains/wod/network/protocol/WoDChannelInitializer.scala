package ru.megains.wod.network.protocol

import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{ChannelInitializer, ChannelOption}
import ru.megains.wod.WoDServer
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.handler.NetHandlerHandshakeTCP

class WoDChannelInitializer(server:WoDServer) extends ChannelInitializer[NioSocketChannel]{


    override def initChannel(ch: NioSocketChannel): Unit = {
        val networkManager = new NetworkManager(server)
        ch.pipeline()
                .addLast("serverCodec", new WoDServerCodec)
                .addLast("messageCodec", new WoDMessageCodec)
                .addLast("packetHandler", networkManager)
        ch.config.setOption(ChannelOption.TCP_NODELAY, Boolean.box(true))


       // NetworkSystem.networkManagers :+= networkManager

        networkManager.setNetHandler(new NetHandlerHandshakeTCP(server, networkManager))
    }
}
