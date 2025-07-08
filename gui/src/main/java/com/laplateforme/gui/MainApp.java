package com.laplateforme.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import com.laplateforme.gui.controller.NotificationController;

public class MainApp extends Application {
  private static Scene scene;
  private static boolean darkMode = true;
  private static StackPane rootStackPane;
  private static Node notificationNode;
  private static Node contentNode;

  @Override
  public void start(Stage stage) throws Exception {
    // Load notification node
    FXMLLoader notifLoader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/notification.fxml"));
    notificationNode = notifLoader.load();
    Object notifController = notifLoader.getController();
    System.out.println("Loaded notification controller: " + notifController);
    // Explicitly set NotificationController singleton
    NotificationController.setInstance((NotificationController) notifController);
    System.out.println("NotificationController.getInstance() after set: " + NotificationController.getInstance());
    // Load initial content (login)
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/login.fxml"));
    contentNode = loader.load();
    // Create persistent root StackPane
    rootStackPane = new StackPane(contentNode, notificationNode);
    scene = new Scene(rootStackPane);
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
    contentNode = loader.load();
    rootStackPane.getChildren().set(0, contentNode); // Always keep notificationNode on top
  }

  public static void setRoot(Parent root) {
    contentNode = root;
    rootStackPane.getChildren().set(0, contentNode);
  }

  public static Scene getScene() {
    return scene;
  }

  public static void main(String[] args) {
    launch(args);
  }
}