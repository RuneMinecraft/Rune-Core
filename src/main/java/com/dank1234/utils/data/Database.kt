package com.dank1234.utils.data

import com.dank1234.utils.Consts
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import java.io.*
import java.sql.*

object Database {
    private val dataSource: HikariDataSource

    init {
        val hikariConfig = HikariConfig()
        val configFile = File("config.yml")
        val config: Map<String, String> = Config.load(configFile)

        try {
            Class.forName("org.mariadb.jdbc.Driver")

            hikariConfig.jdbcUrl = Consts.JDBC_URL // config["database.host"] + config["database.schema"]
            hikariConfig.username = Consts.USERNAME // config["database.user"]
            hikariConfig.password = Consts.PASSWORD // config["database.password"]

            hikariConfig.maximumPoolSize = 10
            hikariConfig.minimumIdle = 2
            hikariConfig.idleTimeout = 30000
            hikariConfig.maxLifetime = 1800000
            hikariConfig.connectionTimeout = 10000
            hikariConfig.connectionTestQuery = "SELECT 1"

            dataSource = HikariDataSource(hikariConfig)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("MariaDB driver not found", e)
        }
    }

    @JvmStatic fun getConnection(): Connection {
        return dataSource.connection
    }
    @JvmStatic fun close(conn: Connection?, stmt: PreparedStatement?, rs: ResultSet? = null) {
        try {
            rs?.close()
            stmt?.close()
            conn?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
    @JvmStatic fun shutdown() {
        if (!dataSource.isClosed) dataSource.close()
    }

    object SQLUtils {
        @JvmStatic fun executeUpdate(
            sql: String
        ): Int {
            return try {
                getConnection().use { conn ->
                    conn.createStatement().use { stmt ->
                        stmt.executeUpdate(sql)
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                0
            }
        }
        @JvmStatic fun executeUpdate(
            sql: String,
            consumer: (PreparedStatement) -> Unit
        ): Int {
            return try {
                getConnection().use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        consumer(pstmt)
                        pstmt.executeUpdate()
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                0
            }
        }
        @JvmStatic fun <T> executeQuery(
            sql: String,
            consumer: (PreparedStatement) -> Unit,
            mapper: (ResultSet) -> T?
        ): T? {
            return try {
                getConnection().use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        consumer(pstmt)
                        pstmt.executeQuery().use { rs ->
                            mapper(rs)
                        }
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                null
            }
        }
    }
}
