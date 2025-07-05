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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import javafx.application.Platform;

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
    String username = usernameField.getText();
    String password = passwordField.getText();
    if (username.isEmpty() || password.isEmpty()) {
      errorLabel.setText("Please enter both username and password.");
      return;
    }
    errorLabel.setText("");
    loginButton.setDisable(true);
    Gson gson = new Gson();
    String json = gson.toJson(new LoginRequest(username, password));
    new Thread(() -> {
      try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Platform.runLater(() -> {
          loginButton.setDisable(false);
          if (response.statusCode() == 200) {
            LoginResponse loginResp = gson.fromJson(response.body(), LoginResponse.class);
            // Switch to main.fxml, passing username and token
            try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/main.fxml"));
              Parent mainRoot = loader.load();
              com.laplateforme.gui.controller.MainController mainController = loader.getController();
              mainController.setUser(username, loginResp.token);
              MainApp.setRoot(mainRoot);
            } catch (Exception e) {
              e.printStackTrace();
              errorLabel.setText("Login succeeded, but failed to load main page.");
            }
          } else if (response.statusCode() == 401) {
            errorLabel.setText("Invalid username or password.");
          } else if (response.statusCode() == 400) {
            errorLabel.setText("Invalid input. Please check your data.");
          } else {
            errorLabel.setText("Login failed. Please try again.");
          }
        });
      } catch (Exception e) {
        Platform.runLater(() -> {
          loginButton.setDisable(false);
          errorLabel.setText("Network error. Please try again.");
        });
      }
    }).start();
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

  // Helper classes for JSON
  private static class LoginRequest {
    String username;
    String password;

    LoginRequest(String username, String password) {
      this.username = username;
      this.password = password;
    }
  }

  private static class LoginResponse {
    String token;
  }
}