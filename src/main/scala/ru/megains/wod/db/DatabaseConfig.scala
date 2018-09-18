package ru.megains.wod.db

case class DatabaseConfig(driver: Option[String], url: Option[String], username: Option[String], password: Option[String], jndiName: Option[String])
