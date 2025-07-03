package com.laplateforme.tracker.dao;

import com.laplateforme.tracker.model.User;
import com.laplateforme.tracker.util.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
  public int insertUser(User user) {
    String sql = "INSERT INTO \"User\" (username, password_hash) VALUES (?, ?) RETURNING id";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPasswordHash());
      ResultSet rs = stmt.executeQuery();
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

  public User findByUsername(String username) {
    String sql = "SELECT id, username, password_hash FROM \"User\" WHERE username = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password_hash"));
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean deleteByUsername(String username) {
    String sql = "DELETE FROM \"User\" WHERE username = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      int rows = stmt.executeUpdate();
      return rows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}