package ru.megains.wod.db

import javax.sql.DataSource

/**
  * Default implementation of the database API using a connection pool.
  */
class PooledDatabase(name: String, databaseConfig: DatabaseConfig, environment: Environment, pool: ConnectionPool)
        extends DefaultDatabase(name, databaseConfig, environment) {

    def this(name: String, databaseConfig: DatabaseConfig) = this(name, databaseConfig, Environment.simple(), new HikariCPConnectionPool(Environment.simple()))

    def createDataSource(): DataSource = {
        pool.create(name, databaseConfig)
    }

    def closeDataSource(dataSource: DataSource): Unit = {
        pool.close(dataSource)
    }

}
