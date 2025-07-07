package com.laplateforme.tracker.util;

import junit.framework.TestCase;
import java.sql.Connection;

public class DatabaseManagerTest extends TestCase {
  public void testGetConnection() throws Exception {
    Connection conn = DatabaseManager.getConnection();
    assertNotNull("Connection should not be null", conn);
    assertFalse("Connection should not be closed", conn.isClosed());
    conn.close();
  }
}