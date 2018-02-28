name := "WoDBaseServer"

version := "1.0"

scalaVersion := "2.12.2"


libraryDependencies ++= Seq(
    "io.netty" % "netty-all" % "5.0.0.Alpha2",
    "com.typesafe.play" %% "anorm" % "2.5.3",
    "com.zaxxer" % "HikariCP" % "2.6.1",
    "org.slf4j" % "slf4j-nop" % "1.7.25",
    "mysql" % "mysql-connector-java" % "5.1.41",
    "org.apache.logging.log4j" % "log4j-core" % "2.8.2",
    "org.apache.logging.log4j" % "log4j-api" % "2.8.2"
)
