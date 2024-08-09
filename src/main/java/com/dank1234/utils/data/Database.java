package com.dank1234.utils.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    private final String USERNAME = "dan";
    private final String PASSWORD = "admin";
    private String JDBC_URL = "jdbc:mysql://localhost:3306/";

    private final String schema;

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

    private Database(String schema) {
        this.schema = schema;

        this.JDBC_URL = this.JDBC_URL() + this.schema();

        try {
            this.connection = DriverManager.getConnection(this.JDBC_URL(), this.USERNAME(), this.PASSWORD());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Database of(String schema) {
        return new Database(schema);
    }
}
