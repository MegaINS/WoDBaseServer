package ru.megains.wod.entity.player

object BodySlot extends Enumeration{
    type BodySlot = Value

    val head = Value("head")
    val gloves = Value("gloves")
    val leggings = Value("leggings")
    val boots = Value("boots")
    val hauberk = Value("hauberk")
    val belt = Value("belt")
    val cuirass = Value("cuirass")
    val shoulders = Value("shoulders")
    val leftHand = Value("leftHand")
    val rightHand = Value("rightHand")
    val none = Value("none")
}
