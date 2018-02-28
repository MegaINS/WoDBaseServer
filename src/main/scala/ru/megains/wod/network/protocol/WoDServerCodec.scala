package ru.megains.wod.network.protocol

import java.util

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec

class WoDServerCodec extends ByteToMessageCodec[ByteBuf]{

    override def encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf): Unit = {

        val i: Int = msg.readableBytes

        out.writeInt(i)
        out.writeBytes(msg)

    }

    override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = {
        in.markReaderIndex

        if(in.readableBytes()>=4){
            val length = in.readInt()
            if (in.readableBytes >= length) {
                out.add(in.readBytes(length))
                return
            }
            in.resetReaderIndex
        }

    }
}
