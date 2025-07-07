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
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = -1;
            try {
              age = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
              System.out.println("[ERROR] Invalid age. Please enter a number.\n");
              break;
            }
            System.out.print("Enter grade: ");
            double grade = -1;
            try {
              grade = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
              System.out.println("[ERROR] Invalid grade. Please enter a number.\n");
              break;
            }
            Student newStudent = new Student(0, firstName, lastName, age, grade);
            int newId = dao.addStudent(newStudent);
            if (newId > 0) {
              System.out.println("[INFO] Student added with ID: " + newId + "\n");
            } else {
              System.out.println("[ERROR] Failed to add student.\n");
            }
            break;
          case "2":
            System.out.print("Enter the ID of the student to update: ");
            int updateId = -1;
            try {
              updateId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
              System.out.println("[ERROR] Invalid ID. Please enter a number.\n");
              break;
            }
            Student existingStudent = dao.getStudentByID(updateId);
            if (existingStudent == null) {
              System.out.println("[ERROR] Student with ID " + updateId + " not found.\n");
              break;
            }
            System.out.println("Leave field blank to keep current value.");
            System.out.print("Enter new first name [" + existingStudent.getFirstName() + "]: ");
            String newFirstName = scanner.nextLine();
            if (newFirstName.isEmpty())
              newFirstName = existingStudent.getFirstName();
            System.out.print("Enter new last name [" + existingStudent.getLastName() + "]: ");
            String newLastName = scanner.nextLine();
            if (newLastName.isEmpty())
              newLastName = existingStudent.getLastName();
            System.out.print("Enter new age [" + existingStudent.getAge() + "]: ");
            String ageInput = scanner.nextLine();
            int newAge = existingStudent.getAge();
            if (!ageInput.isEmpty()) {
              try {
                newAge = Integer.parseInt(ageInput);
              } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid age. Please enter a number.\n");
                break;
              }
            }
            System.out.print("Enter new grade [" + existingStudent.getGrade() + "]: ");
            String gradeInput = scanner.nextLine();
            double newGrade = existingStudent.getGrade();
            if (!gradeInput.isEmpty()) {
              try {
                newGrade = Double.parseDouble(gradeInput);
              } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid grade. Please enter a number.\n");
                break;
              }
            }
            Student updatedStudent = new Student(updateId, newFirstName, newLastName, newAge, newGrade);
            boolean updated = dao.updateStudent(updatedStudent);
            if (updated) {
              System.out.println("[INFO] Student updated successfully.\n");
            } else {
              System.out.println("[ERROR] Failed to update student.\n");
            }
            break;
          case "3":
            System.out.print("Enter the ID of the student to delete: ");
            int deleteId = -1;
            try {
              deleteId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
              System.out.println("[ERROR] Invalid ID. Please enter a number.\n");
              break;
            }
            Student studentToDelete = dao.getStudentByID(deleteId);
            if (studentToDelete == null) {
              System.out.println("[ERROR] Student with ID " + deleteId + " not found.\n");
              break;
            }
            System.out.print("Are you sure you want to delete student '" + studentToDelete.getFirstName() + " "
                + studentToDelete.getLastName() + "' (ID: " + deleteId + ")? (y/N): ");
            String confirm = scanner.nextLine();
            if (!confirm.equalsIgnoreCase("y")) {
              System.out.println("[INFO] Deletion cancelled.\n");
              break;
            }
            boolean deleted = dao.deleteStudent(deleteId);
            if (deleted) {
              System.out.println("[INFO] Student deleted successfully.\n");
            } else {
              System.out.println("[ERROR] Failed to delete student.\n");
            }
            break;
          case "4":
            System.out.print("Enter the ID of the student to view: ");
            int viewId = -1;
            try {
              viewId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
              System.out.println("[ERROR] Invalid ID. Please enter a number.\n");
              break;
            }
            Student student = dao.getStudentByID(viewId);
            if (student == null) {
              System.out.println("[ERROR] Student with ID " + viewId + " not found.\n");
            } else {
              System.out.println("\n--- Student Details ---");
              System.out.println("ID: " + student.getId());
              System.out.println("First Name: " + student.getFirstName());
              System.out.println("Last Name: " + student.getLastName());
              System.out.println("Age: " + student.getAge());
              System.out.println("Grade: " + student.getGrade());
              System.out.println();
            }
            break;
          case "5":
            List<Student> students = dao.getAllStudents();
            if (students.isEmpty()) {
              System.out.println("[INFO] No students found.\n");
            } else {
              System.out.println("\n--- All Students ---");
              for (Student s : students) {
                System.out.println("ID: " + s.getId());
                System.out.println("First Name: " + s.getFirstName());
                System.out.println("Last Name: " + s.getLastName());
                System.out.println("Age: " + s.getAge());
                System.out.println("Grade: " + s.getGrade());
                System.out.println("----------------------");
              }
              System.out.println();
            }
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