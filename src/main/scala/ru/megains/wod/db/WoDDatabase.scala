package ru.megains.wod.db

object WoDDatabase {

    private val URL = "jdbc:mysql://localhost/wod?useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Omsk"
    private val LOGIN = "Bukka"
    private val PASS = "04041992q"

    val db = Databases("com.mysql.jdbc.Driver", URL, "default", LOGIN, PASS)
}
