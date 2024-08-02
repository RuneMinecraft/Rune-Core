package com.dank1234.utils.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;

    private final String JDBC_URL = "jdbc:mysql://82.19.55.80:3306/";
    private final String USERNAME = "dan";
    private final String PASSWORD = "admin";

    private final String schema;
    private final String table;

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
    public String table() {
        return this.table;
    }

    private Database(String schema, String table) {
        try {
            this.connection = DriverManager.getConnection(this.JDBC_URL(), this.USERNAME(), this.PASSWORD());
        }catch (SQLException e) {
            e.printStackTrace();
        }

        this.schema = schema;
        this.table = table;
    }
    public static Database of(String schema, String table) {
        return new Database(schema, table);
    }

    private void checkSchema() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + this.schema());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
