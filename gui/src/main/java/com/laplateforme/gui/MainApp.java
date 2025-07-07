package com.laplateforme.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
  private static Scene scene;
  private static boolean darkMode = true;

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/login.fxml"));
    Parent root = loader.load();
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

  public static void setRoot(String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/laplateforme/gui/fxml/" + fxml));
    Parent root = loader.load();
    scene.setRoot(root);
  }

  public static void setRoot(Parent root) {
    scene.setRoot(root);
  }

  public static Scene getScene() {
    return scene;
  }

  public static void main(String[] args) {
    launch(args);
  }
}