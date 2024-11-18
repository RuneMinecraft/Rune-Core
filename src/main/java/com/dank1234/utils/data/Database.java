package com.dank1234.utils.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private static final String USERNAME = "sftpUser";
    private static final String PASSWORD = "B24Z69K11M";
    private static final String BASE_JDBC_URL = "jdbc:mysql://90.204.54.189:3306/";

    private final String jdbcUrl;
    private final String schema;

    private Database(String schema) {
        this.schema = schema;
        this.jdbcUrl = BASE_JDBC_URL + this.schema;

        try {
            this.connection = DriverManager.getConnection(this.jdbcUrl, USERNAME, PASSWORD);
            System.out.println("Connected to the database: " + this.jdbcUrl);
        }catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static Database of(String schema) {
        return new Database(schema);
    }

    public Connection getConnection() {
        return this.connection;
    }

    public String getSchema() {
        return this.schema;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
                e.printStackTrace();
            } finally {
                this.connection = null;
            }
        }
    }
}
