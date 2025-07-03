package com.laplateforme.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
  private static Scene scene;
  private static boolean darkMode = true;

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/com/laplateforme/gui/fxml/login.fxml"));
    scene = new Scene(root);
    setUserAgentStylesheet(null);
    applyTheme();
    stage.setTitle("Student Tracker - Login");
    stage.setScene(scene);
    stage.show();
  }

  public static void toggleTheme() {
    darkMode = !darkMode;
    applyTheme();
  }

  private static void applyTheme() {
    scene.getStylesheets().clear();
    if (darkMode) {
      scene.getStylesheets().add(MainApp.class.getResource("/com/laplateforme/gui/css/style.css").toExternalForm());
      scene.getRoot().setStyle("-fx-base: #121212;");
    } else {
      scene.getStylesheets().add(MainApp.class.getResource("/com/laplateforme/gui/css/style.css").toExternalForm());
      scene.getRoot().setStyle("-fx-base: #FFFFFF;");
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}