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
        boolean result = studentDAO.addStudent(student);
        assertTrue("Student should be added successfully", result);
    }
}
