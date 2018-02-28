package ru.megains.wod.entity.db

object WoDDatabase {

    private val URL = "jdbc:mysql://localhost/wod?characterEncoding=UTF-8"
    private val LOGIN = "root"
    private val PASS = ""

    val db = Databases("com.mysql.jdbc.Driver", URL, "default", LOGIN, PASS)
}
