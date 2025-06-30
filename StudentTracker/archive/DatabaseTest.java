package com.laplateforme.tracker.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
  public static void main(String[] args) {
    Connection conn = null;
    try {
      conn = DatabaseManager.getConnection();
      if (conn != null && !conn.isClosed()) {
        System.out.println("✅ Successfully connected to the database!");
      } else {
        System.out.println("❌ Failed to connect to the database.");
      }
    } catch (SQLException e) {
      System.err.println("❌ Database connection error: " + e.getMessage());
    } finally {
      DatabaseManager.close(conn);
    }
  }
}