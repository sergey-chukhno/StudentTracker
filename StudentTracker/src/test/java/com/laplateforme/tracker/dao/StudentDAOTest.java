package com.laplateforme.tracker.dao;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseManager;
import junit.framework.TestCase;
import java.sql.Connection;

public class StudentDAOTest extends TestCase {
    private Connection connection;
    private StudentDAO studentDAO;

    protected void setUp() throws Exception {
        super.setUp();
        connection = DatabaseManager.getConnection();
        studentDAO = new StudentDAO();
    }

    protected void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        super.tearDown();
    }

    public void testAddStudent() throws Exception {
        Student student = new Student(0, "Jacques", "Prevost", 121, 17.0);
        int id = studentDAO.addStudent(student);
        assertTrue("Student should be added successfully", id > 0);
    }

    public void testUpdateStudent() throws Exception {
        // First, add a student
        Student student = new Student(0, "Aloise", "Pasquale", 20, 15.5);
        int id = studentDAO.addStudent(student);
        assertTrue("Student should be added successfully", id > 0);

        // Update the student using the returned id
        Student updatedStudent = new Student(id, "Alicia", "Smythe", 21, 18.0);
        boolean updated = studentDAO.updateStudent(updatedStudent);
        assertTrue("Student should be updated successfully", updated);
    }

    public void testDeleteStudent() throws Exception {
        // Add a student
        Student student = new Student(0, "Pauk", "Mirabeau", 22, 12.0);
        int id = studentDAO.addStudent(student);
        assertTrue("Student should be added successfully", id > 0);

        // Delete the student
        boolean deleted = studentDAO.deleteStudent(id);
        assertTrue("Student should be deleted successfully", deleted);
    }

    public void testGetStudentByID() throws Exception {
        // Add a student
        Student student = new Student(0, "Andre", "Citroen", 23, 19.5);
        int id = studentDAO.addStudent(student);
        assertTrue("Student should be added successfully", id > 0);

        // Retrieve the student by id
        Student retrieved = studentDAO.getStudentByID(id);
        assertNotNull("Student should be found by id", retrieved);
        assertEquals("First name should match", student.getFirstName(), retrieved.getFirstName());
        assertEquals("Last name should match", student.getLastName(), retrieved.getLastName());
        assertEquals("Age should match", student.getAge(), retrieved.getAge());
        assertEquals("Grade should match", student.getGrade(), retrieved.getGrade());
    }

    public void testGetAllStudents() throws Exception {
        // Add two students
        Student student1 = new Student(0, "Louis", "Renault", 24, 18.5);
        Student student2 = new Student(0, "Pierre", "Peugeot", 25, 19.0);
        int id1 = studentDAO.addStudent(student1);
        int id2 = studentDAO.addStudent(student2);
        assertTrue("First student should be added", id1 > 0);
        assertTrue("Second student should be added", id2 > 0);

        // Retrieve all students
        java.util.List<Student> students = studentDAO.getAllStudents();
        boolean found1 = false, found2 = false;
        for (Student s : students) {
            if (s.getId() == id1 && s.getFirstName().equals(student1.getFirstName())
                    && s.getLastName().equals(student1.getLastName())) {
                found1 = true;
            }
            if (s.getId() == id2 && s.getFirstName().equals(student2.getFirstName())
                    && s.getLastName().equals(student2.getLastName())) {
                found2 = true;
            }
        }
        assertTrue("First student should be in the list", found1);
        assertTrue("Second student should be in the list", found2);
    }
}
