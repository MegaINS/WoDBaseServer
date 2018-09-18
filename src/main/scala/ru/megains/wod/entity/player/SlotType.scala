package ru.megains.wod.entity.player

object SlotType extends Enumeration{
    type SlotType = Value

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
    val elixir = Value("elixir")
    val none = Value("none")
}
