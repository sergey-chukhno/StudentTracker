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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import javafx.application.Platform;

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
    errorLabel.setText("");
    registerButton.setDisable(true);
    // Prepare JSON
    Gson gson = new Gson();
    String json = gson.toJson(new RegisterRequest(username, password));
    // Send HTTP request in background
    new Thread(() -> {
      try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/register"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Platform.runLater(() -> {
          registerButton.setDisable(false);
          if (response.statusCode() == 201) {
            errorLabel.setStyle("-fx-text-fill: #388e3c;");
            errorLabel.setText("Registration successful! Please log in.");
            // Optionally, switch to login page after a short delay
            new Thread(() -> {
              try {
                Thread.sleep(1200);
              } catch (InterruptedException ignored) {
              }
              Platform.runLater(() -> {
                try {
                  MainApp.setRoot("login.fxml");
                } catch (Exception e) {
                  e.printStackTrace();
                }
              });
            }).start();
          } else if (response.statusCode() == 409) {
            errorLabel.setStyle("");
            errorLabel.setText("Username already exists.");
          } else if (response.statusCode() == 400) {
            errorLabel.setStyle("");
            errorLabel.setText("Invalid input. Please check your data.");
          } else {
            errorLabel.setStyle("");
            errorLabel.setText("Registration failed. Please try again.");
          }
        });
      } catch (Exception e) {
        Platform.runLater(() -> {
          registerButton.setDisable(false);
          errorLabel.setStyle("");
          errorLabel.setText("Network error. Please try again.");
        });
      }
    }).start();
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

  // Helper class for JSON serialization
  private static class RegisterRequest {
    String username;
    String password;

    RegisterRequest(String username, String password) {
      this.username = username;
      this.password = password;
    }
  }
}