package com.dank1234.utils.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Database {
    private static HikariDataSource dataSource;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        Config config = Config.get();

        try {
            Class.forName("org.mariadb.jdbc.Driver");

            hikariConfig.setJdbcUrl(config.getValue("database.host", String.class) + config.getValue("database.schema", String.class));
            hikariConfig.setUsername((config.getValue("database.user", String.class)));
            hikariConfig.setPassword((config.getValue("database.password", String.class)));

            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(2);
            hikariConfig.setIdleTimeout(30000);
            hikariConfig.setMaxLifetime(1800000);
            hikariConfig.setConnectionTimeout(10000);

            hikariConfig.setConnectionTestQuery("SELECT 1");

            dataSource = new HikariDataSource(hikariConfig);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MariaDB driver not found", e);
        }
    }

    private Database() {

    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection conn, PreparedStatement stmt) {
        close(conn, stmt, null);
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
