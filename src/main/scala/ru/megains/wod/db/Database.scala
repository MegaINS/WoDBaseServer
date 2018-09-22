package ru.megains.wod.db

import java.sql.Connection

trait Database {


    def withConnection[A](block: Connection => A): A = {
        withConnection(autocommit = true)(block)
    }
    def withConnection[A](autocommit: Boolean)(block: Connection => A): A = {
        val connection  = WoDDatabase.getConnection
        try {
            block(connection)
        } finally {
            connection.close()
        }
    }
}
