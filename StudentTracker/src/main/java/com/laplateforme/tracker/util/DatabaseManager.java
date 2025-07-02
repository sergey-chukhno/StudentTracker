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
      return DriverManager.getConnection(jdbcUrl, user, password);
    } catch (Exception e) {
      System.out.println("[ERROR] Exception while connecting to DB: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }
}