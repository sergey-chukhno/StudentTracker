package com.laplateforme.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class NotificationController {
  private static NotificationController instance;

  @FXML
  private HBox notificationBox;

  @FXML
  private Label notificationLabel;

  private PauseTransition hideTimer;

  @FXML
  private void initialize() {
    instance = this;
    System.out.println("NotificationController initialized");
    hideTimer = new PauseTransition(Duration.seconds(2.5));
    hideTimer.setOnFinished(e -> hide());
    // Position as toast (top center)
    if (notificationBox.getParent() instanceof StackPane) {
      StackPane.setAlignment(notificationBox, Pos.TOP_CENTER);
      notificationBox.setMaxWidth(400);
    }
  }

  public static NotificationController getInstance() {
    return instance;
  }

  public static void setInstance(NotificationController controller) {
    instance = controller;
  }

  public void setMessage(String message) {
    System.out.println("NotificationController.setMessage: " + message);
    notificationLabel.setText(message);
  }

  public void show() {
    System.out.println("NotificationController.show()");
    notificationBox.setVisible(true);
    notificationBox.toFront();
    hideTimer.playFromStart();
  }

  public void hide() {
    notificationBox.setVisible(false);
  }
}