package ru.megains.wod.network.packet

object Status extends Enumeration{
    type Status = Value
    val success,
        failed = Value
}
