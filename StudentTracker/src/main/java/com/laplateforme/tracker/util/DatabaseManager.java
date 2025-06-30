package com.laplateforme.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * DatabaseManager handles the connection to the PostgreSQL database.
 * It loads configuration from db.properties and provides methods to get and
 * close connections.
 */
public class DatabaseManager {
  private static final String PROPERTIES_FILE = "/db.properties";

  /**
   * Establishes and returns a new database connection using credentials from
   * db.properties.
   * 
   * @return Connection to the PostgreSQL database
   * @throws SQLException if a database access error occurs
   */
  public static Connection getConnection() throws SQLException {
    Properties props = new Properties();
    try (InputStream input = DatabaseManager.class.getResourceAsStream(PROPERTIES_FILE)) {
      if (input == null) {
        throw new IOException("Unable to find " + PROPERTIES_FILE);
      }
      props.load(input);
    } catch (IOException e) {
      throw new SQLException("Failed to load database properties: " + e.getMessage(), e);
    }

    String url = props.getProperty("db.url");
    String user = props.getProperty("db.user");
    String password = props.getProperty("db.password");

    try {
      Class.forName("org.postgresql.Driver"); // Ensure driver is loaded
    } catch (ClassNotFoundException e) {
      throw new SQLException("PostgreSQL JDBC Driver not found.", e);
    }

    return DriverManager.getConnection(url, user, password);
  }

  /**
   * Safely closes the given database connection.
   * 
   * @param conn the Connection to close
   */
  public static void close(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        System.err.println("Failed to close connection: " + e.getMessage());
      }
    }
  }
}