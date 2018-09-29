package ru.megains.wod.item

object ItemParam extends Enumeration  {
    type ItemParam = Value
    val hp = Value("hp")
    val power = Value("pov")
    val minDam = Value("mindam")
    val maxDam = Value("maxdam")
    val int = Value("int")
    val pro = Value("pro")
    val agi = Value("agi")
    val vamp = Value("vamp")
    val krit = Value("krit")
    val slotSize = Value("slotsize")
    val timeAct = Value("timeact")

}
