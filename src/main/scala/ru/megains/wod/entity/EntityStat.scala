package ru.megains.wod.entity

object EntityStat extends Enumeration{

    type EntityStat = Value
    val hpMax= Value("mhp")
    val hp = Value("hp")
    val pow = Value("pov")
    val minDam = Value("mindam")
    val maxDam = Value("maxdam")

}