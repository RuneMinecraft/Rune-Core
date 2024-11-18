package com.dank1234.utils.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Config config = Config.get();
    private Connection connection;

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

    private Database() {
        this.JDBC_URL = this.JDBC_URL() + this.schema();

        try {
            this.connection = DriverManager.getConnection(this.JDBC_URL(), this.USERNAME(), this.PASSWORD());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Database of() {
        return new Database();
    }
}
