package com.laplateforme.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.laplateforme.gui.MainApp;

public class LoginController {
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;
  @FXML
  private Hyperlink registerLink;
  @FXML
  private Label errorLabel;
  @FXML
  private ImageView logoImage;
  @FXML
  private Button themeToggle;
  private boolean darkMode = true;

  @FXML
  private void initialize() {
    errorLabel.setText("");
    // Load logo
    logoImage.setImage(new Image(getClass().getResource("/com/laplateforme/gui/assets/logo.png").toExternalForm()));
    // Set initial icon for theme toggle
    updateThemeIcon();
    themeToggle.setVisible(true);
  }

  @FXML
  private void handleLogin(ActionEvent event) {
    // TODO: Implement login logic (call backend)
    String username = usernameField.getText();
    String password = passwordField.getText();
    if (username.isEmpty() || password.isEmpty()) {
      errorLabel.setText("Please enter both username and password.");
      return;
    }
    // Simulate login success for now
    errorLabel.setText("");
    // TODO: Navigate to main dashboard
  }

  @FXML
  private void handleRegisterLink(ActionEvent event) {
    try {
      MainApp.setRoot("register.fxml");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleThemeToggle(ActionEvent event) {
    darkMode = !darkMode;
    Scene scene = themeToggle.getScene();
    if (darkMode) {
      scene.getRoot().getStyleClass().remove("light-mode");
    } else {
      if (!scene.getRoot().getStyleClass().contains("light-mode")) {
        scene.getRoot().getStyleClass().add("light-mode");
      }
    }
    updateThemeIcon();
  }

  private void updateThemeIcon() {
    String iconPath = darkMode ? "/com/laplateforme/gui/assets/moon.png" : "/com/laplateforme/gui/assets/sun.png";
    ImageView icon = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
    icon.setFitWidth(28);
    icon.setFitHeight(28);
    themeToggle.setGraphic(icon);
  }
}