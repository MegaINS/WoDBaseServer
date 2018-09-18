package ru.megains.wod.db

import java.sql.{Connection, Driver, DriverManager}
import javax.sql.DataSource

import scala.util.control.{ControlThrowable, NonFatal}

/**
  * Default implementation of the database API.
  * Provides driver registration and connection methods.
  */
abstract class DefaultDatabase(val name: String, databaseConfig: DatabaseConfig, environment: Environment) extends Database {



    // abstract methods to be implemented

    def createDataSource(): DataSource

    def closeDataSource(dataSource: DataSource): Unit

    // driver registration

    lazy val driver: Option[Driver] = {
        databaseConfig.driver.map { driverClassName =>
            try {
                val proxyDriver = new ProxyDriver(Reflect.createInstance[Driver](driverClassName, environment.classLoader))
                DriverManager.registerDriver(proxyDriver)
                proxyDriver
            } catch {
                case NonFatal(e) => throw new Exception( s"Driver not found: [$driverClassName}]", e)
            }
        }
    }

    // lazy data source creation

    lazy val dataSource: DataSource = {
        driver // trigger driver registration
        createDataSource()
    }

    lazy val url: String = {
        val connection = dataSource.getConnection
        try {
            connection.getMetaData.getURL
        } finally {
            connection.close()
        }
    }

    // connection methods

    def getConnection(): Connection = {
        getConnection(autocommit = true)
    }

    def getConnection(autocommit: Boolean): Connection = {
        val connection = dataSource.getConnection
        connection.setAutoCommit(autocommit)
        connection
    }

    def withConnection[A](block: Connection => A): A = {
        withConnection(autocommit = true)(block)
    }

    def withConnection[A](autocommit: Boolean)(block: Connection => A): A = {
        val connection = getConnection(autocommit)
        try {
            block(connection)
        } finally {
            connection.close()
        }
    }

    def withTransaction[A](block: Connection => A): A = {
        withConnection(autocommit = false) { connection =>
            try {
                val r = block(connection)
                connection.commit()
                r
            } catch {
                case e: ControlThrowable =>
                    connection.commit()
                    throw e
                case e: Throwable =>
                    connection.rollback()
                    throw e
            }
        }
    }

    // shutdown

    def shutdown(): Unit = {
        closeDataSource(dataSource)
        deregisterDriver()
    }

    def deregisterDriver(): Unit = {
        driver.foreach(DriverManager.deregisterDriver)
    }

}
