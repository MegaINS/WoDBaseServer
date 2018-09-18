package ru.megains.wod.db

object Databases {

    /**
      * Create a pooled database named "default" with the given driver and url.
      *
      * @param driver the database driver class
      * @param url the database url
      * @param name the database name
      *
      * @return a configured database
      */
    def apply(driver: String, url: String, name: String = "default",userName:String, password:String): Database = {
        val dbConfig = DatabaseConfig(Option(driver),Option(url),Option(userName),Option(password),Option(""))

        new PooledDatabase(name, dbConfig)
    }

//    /**
//      * Create an in-memory H2 database.
//      *
//      * @param name the database name (defaults to "default")
//      * @param urlOptions a map of extra url options
//      * @param config a map of extra database configuration
//      * @return a configured in-memory h2 database
//      */
////    def inMemory(name: String = "default", urlOptions: Map[String, String] = Map.empty, config: Map[String, _ <: Any] = Map.empty): Database = {
////        val driver = "org.h2.Driver"
////        val urlExtra = urlOptions.map { case (k, v) => k + "=" + v }.mkString(";", ";", "")
////        val url = "jdbc:h2:mem:" + name + urlExtra
////        Databases(driver, url, name, config)
////    }

//    /**
//      * Run the given block with a database, cleaning up afterwards.
//      *
//      * @param driver the database driver class
//      * @param url the database url
//      * @param name the database name
//      * @param config a map of extra database configuration
//      * @param block The block of code to run
//      * @return The result of the block
//      */
//    def withDatabase[T](driver: String, url: String, name: String = "default",
//                        config: Map[String, _ <: Any] = Map.empty)(block: Database => T): T = {
//        val database = Databases(driver, url, name, config)
//        try {
//            block(database)
//        } finally {
//            database.shutdown()
//        }
//    }

//    /**
//      * Run the given block with an in-memory h2 database, cleaning up afterwards.
//      *
//      * @param name the database name (defaults to "default")
//      * @param urlOptions a map of extra url options
//      * @param config a map of extra database configuration
//      * @param block The block of code to run
//      * @return The result of the block
//      */
//    def withInMemory[T](name: String = "default", urlOptions: Map[String, String] = Map.empty,
//                        config: Map[String, _ <: Any] = Map.empty)(block: Database => T): T = {
//        val database = inMemory(name, urlOptions, config)
//        try {
//            block(database)
//        } finally {
//            database.shutdown()
//        }
//    }
}



