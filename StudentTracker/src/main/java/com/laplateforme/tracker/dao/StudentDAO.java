package com.laplateforme.tracker.dao;

import com.laplateforme.tracker.model.Student;
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
   * @return true if successful, false otherwise
   */
  public boolean addStudent(Student student) {
    // TODO: Implement insert logic
    return false;
  }

  /**
   * Updates an existing student in the database.
   * 
   * @param student the student to update
   * @return true if successful, false otherwise
   */
  public boolean updateStudent(Student student) {
    // TODO: Implement update logic
    return false;
  }

  /**
   * Deletes a student by ID.
   * 
   * @param id the student ID
   * @return true if successful, false otherwise
   */
  public boolean deleteStudent(int id) {
    // TODO: Implement delete logic
    return false;
  }

  /**
   * Retrieves a student by ID.
   * 
   * @param id the student ID
   * @return the Student object, or null if not found
   */
  public Student getStudent(int id) {
    // TODO: Implement select by ID logic
    return null;
  }

  /**
   * Retrieves all students from the database.
   * 
   * @return a list of all students
   */
  public List<Student> getAllStudents() {
    // TODO: Implement select all logic
    return null;
  }
}