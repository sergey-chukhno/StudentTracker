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
  private void initialize() {
    if (logoImage != null) {
      logoImage.setImage(new Image(getClass().getResource("/com/laplateforme/gui/assets/logo.png").toExternalForm()));
    }
    if (paginationCombo != null) {
      paginationCombo.getItems().addAll(10, 25, 50);
      paginationCombo.setValue(10);
    }
  }
}