package com.laplateforme.tracker.service;

import com.laplateforme.tracker.dao.StudentDAO;
import com.laplateforme.tracker.model.Student;
import java.util.List;

public class StudentService {
  private final StudentDAO studentDAO;

  public StudentService() {
    this.studentDAO = new StudentDAO();
  }

  public List<Student> getAllStudents() {
    return studentDAO.getAllStudents();
  }

  public Student getStudentById(int id) {
    return studentDAO.getStudentByID(id);
  }
}