package ru.megains.wod.db

import javax.sql.DataSource

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}



/**
  * HikariCP components (for compile-time injection).
  */
//trait HikariCPComponents {
//    def environment: Environment
//
//    lazy val connectionPool: ConnectionPool = new HikariCPConnectionPool(environment)
//}


class HikariCPConnectionPool (environment: Environment) extends ConnectionPool {


    /**
      * Create a data source with the given configuration.
      *
      * @param name the database name
      * @return a data source backed by a connection pool
      */
    override def create(name: String, dbConfig: DatabaseConfig): DataSource = {

        println(s"Creating Pool for datasource '$name'")
        Try {
            new HikariDataSource(new HikariCPConfig(dbConfig).toHikariConfig)
//            // Bind in JNDI
//            dbConfig.jndiName.foreach { jndiName =>
//                JNDI.initialContext.rebind(jndiName, datasource)
//                logger.info(s"datasource [$name] bound to JNDI as $jndiName")
//            }
        } match {
            case Success(datasource) => datasource
            case Failure(ex) => throw new Exception(ex.getMessage, ex)
        }
    }

    /**
      * Close the given data source.
      *
      * @param dataSource the data source to close
      */
    override def close(dataSource: DataSource) = {
        println("Shutting down connection pool.")
        dataSource match {
            case ds: HikariDataSource => ds.close()
            case _ => sys.error("Unable to close data source: not a HikariDataSource")
        }
    }
}

/**
  * HikariCP config
  */
class HikariCPConfig(dbConfig: DatabaseConfig) {

    def toHikariConfig: HikariConfig = {
        val hikariConfig = new HikariConfig()

       // val config = configuration.get[PlayConfig]("hikaricp")

        // Essentials configurations

       // hikariConfig.setDataSourceClassName(null)

        dbConfig.url.foreach(hikariConfig.setJdbcUrl)
        dbConfig.driver.foreach(hikariConfig.setDriverClassName)

        dbConfig.username.foreach(hikariConfig.setUsername)
        dbConfig.password.foreach(hikariConfig.setPassword)


//        val dataSourceConfig = config.get[PlayConfig]("dataSource")
//        dataSourceConfig.underlying.root().keySet().asScala.foreach { key =>
//            hikariConfig.addDataSourceProperty(key, dataSourceConfig.get[String](key))
//        }

        def toMillis(duration: Duration) = {
            if (duration.isFinite()) duration.toMillis
            else 0l
        }

        // Frequently used
        hikariConfig.setAutoCommit(true)
        hikariConfig.setConnectionTimeout(toMillis( Duration("30 sec")))
        hikariConfig.setIdleTimeout(toMillis( Duration("10 min")))
        hikariConfig.setMaxLifetime(toMillis(Duration("30 min")))

      //  config.get[Option[String]]("connectionTestQuery").foreach(hikariConfig.setConnectionTestQuery)
       // config.get[Option[Int]]("minimumIdle").foreach(hikariConfig.setMinimumIdle)
        hikariConfig.setMaximumPoolSize(10)
       // config.get[Option[String]]("poolName").foreach(hikariConfig.setPoolName)

        // Infrequently used
        hikariConfig.setInitializationFailTimeout(1)
       // hikariConfig.setInitializationFailFast(config.get[Boolean]("initializationFailFast"))
        hikariConfig.setIsolateInternalQueries(false)
        hikariConfig.setAllowPoolSuspension(false)
        hikariConfig.setReadOnly(false)
        hikariConfig.setRegisterMbeans(false)
      //  config.get[Option[String]]("connectionInitSql").foreach(hikariConfig.setConnectionInitSql)
      //  config.get[Option[String]]("catalog").foreach(hikariConfig.setCatalog)
       // config.get[Option[String]]("transactionIsolation").foreach(hikariConfig.setTransactionIsolation)
        hikariConfig.setValidationTimeout(toMillis(  Duration("5 sec")))
        hikariConfig.setLeakDetectionThreshold(1)

        hikariConfig.validate()
        hikariConfig
    }
}


