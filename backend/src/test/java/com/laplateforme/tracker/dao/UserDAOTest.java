package com.laplateforme.tracker.dao;

import com.laplateforme.tracker.model.User;
import junit.framework.TestCase;

public class UserDAOTest extends TestCase {
  private UserDAO userDAO;

  @Override
  protected void setUp() throws Exception {
    userDAO = new UserDAO();
    userDAO.deleteByUsername("testuser");
    userDAO.deleteByUsername("testuserdup");
  }

  @Override
  protected void tearDown() throws Exception {
    userDAO.deleteByUsername("testuser");
    userDAO.deleteByUsername("testuserdup");
    // Assert cleanup
    assertNull(userDAO.findByUsername("testuser"));
    assertNull(userDAO.findByUsername("testuserdup"));
  }

  public void testInsertAndFindUser() throws Exception {
    String username = "testuser";
    String passwordHash = "$2a$10$abcdefghijklmnopqrstuv"; // fake hash
    User user = new User(0, username, passwordHash);
    int id = userDAO.insertUser(user);
    assertTrue("User should be inserted and get a positive id", id > 0);
    User fetched = userDAO.findByUsername(username);
    assertNotNull("User should be found by username", fetched);
    assertEquals(username, fetched.getUsername());
    assertEquals(passwordHash, fetched.getPasswordHash());
  }

  public void testDuplicateUsername() throws Exception {
    String username = "testuserdup";
    String passwordHash = "$2a$10$abcdefghijklmnopqrstuv";
    User user1 = new User(0, username, passwordHash);
    int id1 = userDAO.insertUser(user1);
    assertTrue(id1 > 0);
    User user2 = new User(0, username, passwordHash);
    int id2 = userDAO.insertUser(user2);
    assertEquals(-1, id2); // Should fail due to unique constraint
  }

  public void testFindByUsernameNotFound() throws Exception {
    User user = userDAO.findByUsername("nonexistentuser");
    assertNull(user);
  }
}