package ru.megains.wod

import ru.megains.wod

object Action extends Enumeration  {
    type Action = Value
    val moveToLcc: wod.Action.Value = Value
    val takeOff: wod.Action.Value = Value
    val take: wod.Action.Value = Value
    val delete: wod.Action.Value = Value
    val store: wod.Action.Value = Value
    val storeBuy: wod.Action.Value = Value
    val attack: wod.Action.Value = Value
    val exitInLoc: wod.Action.Value = Value
    val loadPlayerLoc: wod.Action.Value = Value



}

