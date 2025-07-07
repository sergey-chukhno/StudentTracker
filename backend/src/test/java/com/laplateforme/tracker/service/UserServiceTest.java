package com.laplateforme.tracker.service;

import com.laplateforme.tracker.dao.UserDAO;
import com.laplateforme.tracker.model.User;
import junit.framework.TestCase;
import org.mockito.Mockito;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceTest extends TestCase {
  private UserDAO userDAOMock;
  private UserService userService;

  @Override
  protected void setUp() {
    userDAOMock = Mockito.mock(UserDAO.class);
    userService = new UserService(userDAOMock);
  }

  public void testRegisterUser_Success() {
    Mockito.when(userDAOMock.findByUsername("bob")).thenReturn(null);
    Mockito.when(userDAOMock.insertUser(Mockito.any(User.class))).thenReturn(1);

    User user = userService.registerUser("bob", "password123");
    assertNotNull(user);
    assertEquals("bob", user.getUsername());
    assertTrue(BCrypt.checkpw("password123", user.getPasswordHash()));
  }

  public void testRegisterUser_Duplicate() {
    Mockito.when(userDAOMock.findByUsername("bob")).thenReturn(new User(1, "bob", "hash"));

    User user = userService.registerUser("bob", "password123");
    assertNull(user);
  }

  public void testRegisterUser_DAOInsertFails() {
    Mockito.when(userDAOMock.findByUsername("bob")).thenReturn(null);
    Mockito.when(userDAOMock.insertUser(Mockito.any(User.class))).thenReturn(-1);

    User user = userService.registerUser("bob", "password123");
    assertNull(user);
  }
}