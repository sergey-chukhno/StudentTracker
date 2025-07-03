package com.laplateforme.tracker.service;

import com.laplateforme.tracker.dao.UserDAO;
import com.laplateforme.tracker.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
  private final UserDAO userDAO = new UserDAO();

  public User registerUser(String username, String password) {
    // Check if username already exists
    if (userDAO.findByUsername(username) != null) {
      return null; // Username taken
    }
    // Hash the password
    String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    User user = new User(0, username, passwordHash);
    int newId = userDAO.insertUser(user);
    if (newId > 0) {
      user.setId(newId);
      return user;
    } else {
      return null;
    }
  }

  public User getUserByUsername(String username) {
    return userDAO.findByUsername(username);
  }
}