package com.laplateforme.gui.controller;

import javafx.application.Platform;

public class NotificationService {
  public static void show(String message) {
    Platform.runLater(() -> {
      NotificationController controller = NotificationController.getInstance();
      System.out.println("NotificationService.show: controller=" + controller);
      if (controller != null) {
        controller.setMessage(message);
        controller.show();
      }
    });
  }

  public static void hide() {
    Platform.runLater(() -> {
      NotificationController controller = NotificationController.getInstance();
      if (controller != null) {
        controller.hide();
      }
    });
  }
}