package ru.megains.wod.network.protocol

import java.io.IOException
import java.util

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import ru.megains.wod.Logger
import ru.megains.wod.network.NetworkManager
import ru.megains.wod.network.packet.{ConnectionState, PacketBuffer, PacketWrite}


class WoDMessageCodec extends ByteToMessageCodec[PacketWrite] with Logger[WoDMessageCodec]{


    override def encode(ctx: ChannelHandlerContext, msg: PacketWrite, out: ByteBuf): Unit = {

        val id = ctx.pipeline().channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get().getServerPacketId(msg.getClass)
        val buffer = new PacketBuffer(out)

        buffer.writeShort(id)
        msg.writePacketData(buffer)
        val size = out.readableBytes()
        val name = ConnectionState.getFromPacket(msg).name
        val packetName = msg.getClass.getSimpleName
        log.info(s"Encoder $name, packet $packetName, id $id, size $size")
    }

    override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = {
        val size = in.readableBytes()
        if (size != 0) {
            val buffer = new PacketBuffer(in)
            val id = buffer.readShort()

            val packet = ctx.pipeline().channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get().getClientPacket(id)

            if (packet == null) throw new IOException("Bad packet id " + id)
            val name = ConnectionState.getFromPacket(packet).name


            val packetName = packet.getClass.getSimpleName

            log.info(s"Decoder $name, packet $packetName, id $id, size $size")
            packet.readPacketData(buffer)

            if (in.readableBytes > 0) throw new IOException("Packet was larger than I expected, found " + in.readableBytes + " bytes extra whilst reading packet " + id)
            else out.add(packet)

        }
    }
}
