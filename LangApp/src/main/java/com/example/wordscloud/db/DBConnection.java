package com.example.wordscloud.db;

import com.example.wordscloud.models.IDBPrefs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    /*public static class Properties {
        String driverPath = "com.mysql.cj.jdbc.Driver";
        String driverPrefix = "jdbc:mysql";
        String database;
        String username;
        String password;
        String host = "localhost";
        String port = "3306";
    }*/

    private final String driverPath;
    private final String username;
    private final String password;
    private final String url;

    // Constructor with only database name, using default username and empty password
    public DBConnection(IDBPrefs properties) {
        // Construct the JDBC URL for the MySQL database
        this.driverPath = properties.get("driver_path");
        this.username = properties.get("login");
        this.password = properties.get("password");
        this.url = String.format("%s://%s:%s/%s", properties.get("driver_prefix"), properties.get("host"), properties.get("port"), properties.get("database"));
    }

    // Get the underlying Connection object for database operations
    public Connection getConnection() {
        try {
            Class.forName(driverPath);
            // Attempt to establish the database connection using DriverManager
            return DriverManager.getConnection(this.url, username, password);
        }
        catch(Exception e) {
            return null;
        }
    }
}
