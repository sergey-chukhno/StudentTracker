package com.laplateforme.tracker.dao;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseManager;
import junit.framework.TestCase;
import java.sql.Connection;

public class StudentDAOTest extends TestCase {
    private Connection connection;
    private StudentDAO studentDAO;
    private java.util.List<Integer> createdStudentIds;

    protected void setUp() throws Exception {
        super.setUp();
        connection = DatabaseManager.getConnection();
        studentDAO = new StudentDAO();
        createdStudentIds = new java.util.ArrayList<>();
    }

    protected void tearDown() throws Exception {
        for (Integer id : createdStudentIds) {
            studentDAO.deleteStudent(id);
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        super.tearDown();
    }

    public void testAddStudent() throws Exception {
        Student student = new Student(0, "Jacques", "Prevost", 121, 17.0);
        int id = studentDAO.addStudent(student);
        createdStudentIds.add(id);
        assertTrue("Student should be added successfully", id > 0);
    }

    public void testUpdateStudent() throws Exception {
        Student student = new Student(0, "Aloise", "Pasquale", 20, 15.5);
        int id = studentDAO.addStudent(student);
        createdStudentIds.add(id);
        assertTrue("Student should be added successfully", id > 0);
        Student updatedStudent = new Student(id, "Alicia", "Smythe", 21, 18.0);
        boolean updated = studentDAO.updateStudent(updatedStudent);
        assertTrue("Student should be updated successfully", updated);
        Student fetched = studentDAO.getStudentByID(id);
        assertEquals("Alicia", fetched.getFirstName());
        assertEquals("Smythe", fetched.getLastName());
        assertEquals(21, fetched.getAge());
        assertEquals(18.0, fetched.getGrade());
    }

    public void testDeleteStudent() throws Exception {
        Student student = new Student(0, "Pauk", "Mirabeau", 22, 12.0);
        int id = studentDAO.addStudent(student);
        createdStudentIds.add(id);
        assertTrue("Student should be added successfully", id > 0);
        boolean deleted = studentDAO.deleteStudent(id);
        assertTrue("Student should be deleted successfully", deleted);
        Student fetched = studentDAO.getStudentByID(id);
        assertNull(fetched);
    }

    public void testGetStudentByID() throws Exception {
        Student student = new Student(0, "Andre", "Citroen", 23, 19.5);
        int id = studentDAO.addStudent(student);
        createdStudentIds.add(id);
        assertTrue("Student should be added successfully", id > 0);
        Student retrieved = studentDAO.getStudentByID(id);
        assertNotNull("Student should be found by id", retrieved);
        assertEquals("First name should match", student.getFirstName(), retrieved.getFirstName());
        assertEquals("Last name should match", student.getLastName(), retrieved.getLastName());
        assertEquals("Age should match", student.getAge(), retrieved.getAge());
        assertEquals("Grade should match", student.getGrade(), retrieved.getGrade());
    }

    public void testGetAllStudents() throws Exception {
        Student student1 = new Student(0, "Louis", "Renault", 24, 18.5);
        Student student2 = new Student(0, "Pierre", "Peugeot", 25, 19.0);
        int id1 = studentDAO.addStudent(student1);
        int id2 = studentDAO.addStudent(student2);
        createdStudentIds.add(id1);
        createdStudentIds.add(id2);
        assertTrue("First student should be added", id1 > 0);
        assertTrue("Second student should be added", id2 > 0);
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
