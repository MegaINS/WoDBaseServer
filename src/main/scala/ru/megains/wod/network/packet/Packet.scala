package ru.megains.wod.network.packet

import ru.megains.wod.network.handler.INetHandler

abstract class Packet[T <: INetHandler] {

    def isImportant = false

}



