package com.laplateforme.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;

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
  private TabPane tabPane;
  @FXML
  private Tab loginTab;
  @FXML
  private Tab registerTab;

  @FXML
  private void initialize() {
    errorLabel.setText("");
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
    if (tabPane != null && loginTab != null) {
      tabPane.getSelectionModel().select(loginTab);
    }
  }
}