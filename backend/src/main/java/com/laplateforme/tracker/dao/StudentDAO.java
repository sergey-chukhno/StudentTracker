package com.laplateforme.tracker.dao;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for Student entity.
 * Handles CRUD operations using DatabaseManager.
 */
public class StudentDAO {
  /**
   * Adds a new student to the database.
   * 
   * @param student the student to add
   * @return the generated id if successful, -1 otherwise
   */
  public int addStudent(Student student) {
    String sql = "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?) RETURNING id";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, student.getFirstName());
      stmt.setString(2, student.getLastName());
      stmt.setInt(3, student.getAge());
      stmt.setDouble(4, student.getGrade());
      java.sql.ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        return -1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * Updates an existing student in the database.
   * 
   * @param student the student to update
   * @return true if successful, false otherwise
   */
  public boolean updateStudent(Student student) {
    String sql = "UPDATE student SET first_name = ?, last_name = ?, age = ?, grade = ? WHERE id = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, student.getFirstName());
      stmt.setString(2, student.getLastName());
      stmt.setInt(3, student.getAge());
      stmt.setDouble(4, student.getGrade());
      stmt.setInt(5, student.getId());
      int rows = stmt.executeUpdate();
      return rows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Deletes a student by ID.
   * 
   * @param id the student ID
   * @return true if successful, false otherwise
   */
  public boolean deleteStudent(int id) {
    String sql = "DELETE FROM student WHERE id = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      int rows = stmt.executeUpdate();
      return rows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Retrieves a student by ID.
   * 
   * @param id the student ID
   * @return the Student object, or null if not found
   */
  public Student getStudentByID(int id) {
    String sql = "SELECT id, first_name, last_name, age, grade FROM student WHERE id = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      java.sql.ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new Student(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getInt("age"),
            rs.getDouble("grade"));
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Retrieves all students from the database.
   * 
   * @return a list of all students
   */
  public java.util.List<Student> getAllStudents() {
    java.util.List<Student> students = new java.util.ArrayList<>();
    String sql = "SELECT id, first_name, last_name, age, grade FROM student";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        java.sql.ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        students.add(new Student(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getInt("age"),
            rs.getDouble("grade")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return students;
  }
}