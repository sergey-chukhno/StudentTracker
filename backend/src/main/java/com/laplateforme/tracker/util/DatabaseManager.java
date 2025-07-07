package com.laplateforme.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseManager {
  private static final String PROPERTIES_FILE = "/db.properties";

  public static Connection getConnection() {
    try {
      System.out.println("[DEBUG] Attempting to load db.properties from classpath...");
      InputStream input = DatabaseManager.class.getResourceAsStream("/db.properties");
      if (input == null) {
        System.out.println("[ERROR] db.properties not found on classpath!");
        return null;
      }
      Properties props = new Properties();
      props.load(input);
      String jdbcUrl = props.getProperty("jdbc.url");
      String user = props.getProperty("jdbc.user");
      String password = props.getProperty("jdbc.password");
      System.out.println("[DEBUG] Loaded JDBC URL: " + jdbcUrl);
      System.out.println("[DEBUG] Loaded DB user: " + user);
      Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
      // Print DB name after connection
      try {
        String dbName = conn.getCatalog();
        System.out.println("[INFO] Connected to database: " + dbName);
      } catch (Exception e) {
        System.out.println("[WARN] Could not retrieve DB name: " + e.getMessage());
      }
      return conn;
    } catch (Exception e) {
      System.out.println("[ERROR] Exception while connecting to DB: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  static {
    try (Connection conn = getConnection()) {
      if (conn != null) {
        System.out.println("[DEBUG] Creating Student table if not exists...");
        String createStudentTable = "CREATE TABLE IF NOT EXISTS Student (" +
            "id SERIAL PRIMARY KEY, " +
            "first_name VARCHAR(255) NOT NULL, " +
            "last_name VARCHAR(255) NOT NULL, " +
            "age INTEGER NOT NULL, " +
            "grade DOUBLE PRECISION NOT NULL" +
            ")";
        conn.createStatement().execute(createStudentTable);
        System.out.println("[DEBUG] Student table creation checked.");

        System.out.println("[DEBUG] Creating User table if not exists...");
        String createUserTable = "CREATE TABLE IF NOT EXISTS \"User\" (" +
            "id SERIAL PRIMARY KEY, " +
            "username VARCHAR(255) UNIQUE NOT NULL, " +
            "password_hash VARCHAR(255) NOT NULL" +
            ")";
        conn.createStatement().execute(createUserTable);
        System.out.println("[DEBUG] User table creation checked.");
      }
    } catch (Exception e) {
      System.out.println("[ERROR] Exception during table creation: " + e.getMessage());
      e.printStackTrace();
    }
  }
}