package com.laplateforme.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.laplateforme.gui.MainApp;

public class RegisterController {
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private Button registerButton;
  @FXML
  private Hyperlink loginLink;
  @FXML
  private Label errorLabel;
  @FXML
  private ImageView logoImage;
  @FXML
  private Button themeToggle;
  private boolean darkMode = true;
  @FXML
  private TabPane tabPane;
  @FXML
  private Tab loginTab;
  @FXML
  private Tab registerTab;

  @FXML
  private void initialize() {
    errorLabel.setText("");
    // Load logo
    if (logoImage != null) {
      logoImage.setImage(new Image(getClass().getResource("/com/laplateforme/gui/assets/logo.png").toExternalForm()));
    }
    // Set initial icon for theme toggle
    if (themeToggle != null) {
      updateThemeIcon();
      themeToggle.setVisible(true);
    }
  }

  @FXML
  private void handleRegister(ActionEvent event) {
    // TODO: Implement registration logic (call backend)
    String username = usernameField.getText();
    String password = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();
    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      errorLabel.setText("Please fill in all fields.");
      return;
    }
    if (!password.equals(confirmPassword)) {
      errorLabel.setText("Passwords do not match.");
      return;
    }
    // Simulate registration success for now
    errorLabel.setText("");
    // TODO: Navigate to login tab
    tabPane.getSelectionModel().select(loginTab);
  }

  @FXML
  private void handleLoginLink(ActionEvent event) {
    try {
      MainApp.setRoot("login.fxml");
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
    if (themeToggle != null)
      themeToggle.setGraphic(icon);
  }
}