package com.laplateforme.tracker;

import com.laplateforme.tracker.dao.StudentDAO;
import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseManager;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.io.InputStream;

public class StudentTrackerCLI {
  public static void main(String[] args) {
    // Read db.properties to get JDBC URL
    String jdbcUrl = null;
    try (InputStream input = StudentTrackerCLI.class.getResourceAsStream("/db.properties")) {
      Properties props = new Properties();
      props.load(input);
      jdbcUrl = props.getProperty("jdbc.url");
    } catch (Exception e) {
      System.out.println("[ERROR] Could not read db.properties: " + e.getMessage());
      return;
    }

    // Parse host and port from JDBC URL
    String host = "localhost";
    String port = "5432";
    if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:postgresql://")) {
      String withoutPrefix = jdbcUrl.substring("jdbc:postgresql://".length());
      String[] parts = withoutPrefix.split("/");
      String hostPort = parts[0];
      String[] hostPortParts = hostPort.split(":");
      host = hostPortParts[0];
      if (hostPortParts.length > 1) {
        port = hostPortParts[1];
      }
    }
    System.out.println("[INFO] Connecting to the database at " + host + ":" + port + " ...");
    try (Connection conn = DatabaseManager.getConnection()) {
      if (conn != null && !conn.isClosed()) {
        System.out.println("[INFO] Database connection successful!\n");
      } else {
        System.out.println("[ERROR] Failed to connect to the database.");
        return;
      }
      StudentDAO dao = new StudentDAO();
      Scanner scanner = new Scanner(System.in);
      while (true) {
        System.out.println("==== Student Tracker Menu ====");
        System.out.println("1. Add Student");
        System.out.println("2. Update Student");
        System.out.println("3. Delete Student");
        System.out.println("4. Get Student by ID");
        System.out.println("5. Get All Students");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();
        switch (choice) {
          case "1":
            // TODO: Add Student logic
            break;
          case "2":
            // TODO: Update Student logic
            break;
          case "3":
            // TODO: Delete Student logic
            break;
          case "4":
            // TODO: Get Student by ID logic
            break;
          case "5":
            // TODO: Get All Students logic
            break;
          case "0":
            System.out.println("Exiting. Goodbye!");
            scanner.close();
            return;
          default:
            System.out.println("Invalid choice. Please try again.\n");
        }
      }
    } catch (Exception e) {
      System.out.println("[ERROR] Database connection error: " + e.getMessage());
    }
  }
}