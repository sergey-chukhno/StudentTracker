package com.laplateforme.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseManager {
  private static final String PROPERTIES_FILE = "/db.properties";

  public static Connection getConnection() {
    try (InputStream input = DatabaseManager.class.getResourceAsStream(PROPERTIES_FILE)) {
      Properties props = new Properties();
      props.load(input);
      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");
      return DriverManager.getConnection(url, username, password);
    } catch (Exception e) {
      throw new RuntimeException("Failed to connect to the database", e);
    }
  }
}