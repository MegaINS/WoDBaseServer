package ru.megains.wod.db

import java.sql.Connection

import scalikejdbc.{ConnectionPool, ConnectionPoolSettings}


object WoDDatabase {

    private val URL = "jdbc:mysql://localhost/wod?useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Omsk"
    private val LOGIN = "Bukka"
    private val PASS = "04041992q"
    private val settings = ConnectionPoolSettings(
        initialSize = 8,
        maxSize = 20,
        connectionTimeoutMillis = 3000L)

    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.add("wod",URL, LOGIN, PASS,settings)

    def getConnection: Connection = ConnectionPool.borrow("wod")

}
