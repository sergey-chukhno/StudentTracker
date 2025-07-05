package com.laplateforme.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;

public class MainController {
  @FXML
  private Label welcomeLabel;
  @FXML
  private ImageView logoImage;

  private String username;
  private String token;

  public void setUser(String username, String token) {
    this.username = username;
    this.token = token;
    if (welcomeLabel != null) {
      welcomeLabel.setText("Welcome " + username);
    }
  }

  @FXML
  private Button dashboardButton;
  @FXML
  private Button analyticsButton;
  @FXML
  private Button exitButton;
  @FXML
  private TextField searchField;
  @FXML
  private ComboBox<Integer> paginationCombo;
  @FXML
  private Button exportPdfButton;
  @FXML
  private StackPane mainFrame;
  @FXML
  private ImageView dashboardIcon;
  @FXML
  private ImageView analyticsIcon;
  @FXML
  private ImageView exitIcon;
  @FXML
  private Button addStudentButton;
  @FXML
  private Button themeToggle;
  private boolean darkMode = true;

  @FXML
  private void initialize() {
    if (logoImage != null) {
      logoImage.setImage(new Image(getClass().getResource("/com/laplateforme/gui/assets/logo.png").toExternalForm()));
    }
    if (dashboardIcon != null) {
      dashboardIcon.setImage(
          new Image(getClass().getResource("/com/laplateforme/gui/assets/icons/dashboard.png").toExternalForm()));
    }
    if (analyticsIcon != null) {
      analyticsIcon.setImage(
          new Image(getClass().getResource("/com/laplateforme/gui/assets/icons/analytics.png").toExternalForm()));
    }
    if (exitIcon != null) {
      exitIcon
          .setImage(new Image(getClass().getResource("/com/laplateforme/gui/assets/icons/exit.png").toExternalForm()));
    }
    if (themeToggle != null) {
      updateThemeIcon();
      themeToggle.setVisible(true);
      themeToggle.setOnAction(e -> handleThemeToggle());
    }
  }

  private void handleThemeToggle() {
    darkMode = !darkMode;
    javafx.scene.Scene scene = themeToggle.getScene();
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