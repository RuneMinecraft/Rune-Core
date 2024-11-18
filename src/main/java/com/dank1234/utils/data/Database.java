package com.dank1234.utils.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public final class Database {
    private static HikariDataSource dataSource;
    private static Config config = Config.get();
    private Connection connection;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            HikariConfig hikariConfig = new HikariConfig();
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
            System.err.println("MariaDB driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private final String USERNAME = config.getValue("database.user", String.class);
    private final String PASSWORD = config.getValue("database.password", String.class);
    private String JDBC_URL = config.getValue("database.host", String.class);
    private final String schema = config.getValue("database.schema", String.class);

    public Connection connection() {
        return this.connection;
    }
    public String JDBC_URL() {
        return this.JDBC_URL;
    }
    public String USERNAME() {
        return this.USERNAME;
    }
    public String PASSWORD() {
        return this.PASSWORD;
    }
    public String schema() {
        return this.schema;
    }

    public Database() {
        this.JDBC_URL = JDBC_URL + this.schema;

        try {
            this.connection = DriverManager.getConnection(this.JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database: " + this.JDBC_URL);
        }catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static Database of() {
        return new Database();
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
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
