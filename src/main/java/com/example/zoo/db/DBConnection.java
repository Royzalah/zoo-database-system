package com.example.zoo.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton connection manager for the zoo_db PostgreSQL database.
 * Credentials are loaded once from db.properties (NOT committed to Git),
 * instead of being typed in at runtime every time.
 */
public class DBConnection {

    private static Connection connection = null;

    private DBConnection() {
        // Prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DBConnection.class) {
                if (connection == null || connection.isClosed()) {

                    Properties props = new Properties();
                    try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                        if (input == null) {
                            throw new SQLException("db.properties not found on the classpath! " +
                                    "Make sure it is placed in src/main/resources/");
                        }
                        props.load(input);
                    } catch (IOException ex) {
                        throw new SQLException("Could not load db.properties file!", ex);
                    }

                    try {
                        Class.forName("org.postgresql.Driver");
                    } catch (ClassNotFoundException e) {
                        throw new SQLException("PostgreSQL JDBC Driver missing from classpath!", e);
                    }

                    connection = DriverManager.getConnection(
                            props.getProperty("db.url"),
                            props.getProperty("db.user"),
                            props.getProperty("db.password")
                    );
                    System.out.println("Database connection established successfully.");
                }
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed cleanly.");
            }
        } catch (SQLException e) {
            System.err.println("Error while closing the database connection.");
            e.printStackTrace();
        }
    }
}