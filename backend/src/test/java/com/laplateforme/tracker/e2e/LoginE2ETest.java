package com.laplateforme.gui.e2e;

import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import static org.testfx.assertions.api.Assertions.*;

public class LoginE2ETest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        // Start your JavaFX application
        new com.laplateforme.gui.MainApp().start(stage);
    }

    @BeforeAll
    public static void startBackend() throws Exception {
        // Optionally start backend server here if not already running
    }

    @Test
    public void testLoginFlow() {
        clickOn("#usernameField").write("e2euser");
        clickOn("#passwordField").write("e2epass");
        clickOn("#loginButton");
        // Wait for main screen, check welcome label, etc.
        verifyThat("#welcomeLabel", hasText("Welcome e2euser"));
    }
}
