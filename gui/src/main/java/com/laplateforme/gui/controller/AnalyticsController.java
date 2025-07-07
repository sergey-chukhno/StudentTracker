package com.laplateforme.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import com.laplateforme.gui.Student;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalDateTime;
import java.util.Optional;

public class AnalyticsController {
  @FXML
  private Label welcomeLabel;
  @FXML
  private ImageView logoImage;
  @FXML
  private Button themeToggle;
  @FXML
  private Button dashboardButton;
  @FXML
  private Button analyticsButton;
  @FXML
  private Button exitButton;
  @FXML
  private ImageView dashboardIcon;
  @FXML
  private ImageView analyticsIcon;
  @FXML
  private ImageView exitIcon;
  @FXML
  private Label totalStudentsLabel;
  @FXML
  private Label averageGradeLabel;
  @FXML
  private PieChart gradePieChart;
  @FXML
  private BarChart<String, Number> gradeBarChart;
  @FXML
  private CategoryAxis barCategoryAxis;
  @FXML
  private NumberAxis barNumberAxis;

  private String username;
  private String token;

  public void setUser(String username, String token) {
    this.username = username;
    this.token = token;
    if (welcomeLabel != null) {
      welcomeLabel.setText("Welcome " + username);
    }
    fetchAndDisplayStats();
  }

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
      themeToggle.setVisible(true);
      themeToggle.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
          updateThemeIcon();
        }
      });
      themeToggle.setOnAction(e -> handleThemeToggle());
    }
    if (dashboardButton != null) {
      dashboardButton.setOnAction(e -> switchToDashboard());
    }
    if (analyticsButton != null) {
      analyticsButton.setDisable(true);
    }
    if (exitButton != null) {
      exitButton.setOnAction(e -> switchToLogin());
    }
  }

  private void handleThemeToggle() {
    javafx.scene.Scene scene = themeToggle.getScene();
    boolean darkMode = !scene.getRoot().getStyleClass().contains("light-mode");
    if (darkMode) {
      scene.getRoot().getStyleClass().add("light-mode");
    } else {
      scene.getRoot().getStyleClass().remove("light-mode");
    }
    updateThemeIcon();
  }

  private void updateThemeIcon() {
    javafx.scene.Scene scene = themeToggle.getScene();
    boolean darkMode = !scene.getRoot().getStyleClass().contains("light-mode");
    String iconPath = darkMode ? "/com/laplateforme/gui/assets/moon.png" : "/com/laplateforme/gui/assets/sun.png";
    ImageView icon = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
    icon.setFitWidth(28);
    icon.setFitHeight(28);
    themeToggle.setGraphic(icon);
  }

  private void switchToDashboard() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/main.fxml"));
      Parent mainRoot = loader.load();
      com.laplateforme.gui.controller.MainController mainController = loader.getController();
      mainController.setUser(username, token);
      Stage stage = (Stage) dashboardButton.getScene().getWindow();
      Scene currentScene = dashboardButton.getScene();
      Scene newScene = new Scene(mainRoot, currentScene.getWidth(), currentScene.getHeight());
      // Add custom stylesheet
      newScene.getStylesheets().add(getClass().getResource("/com/laplateforme/gui/css/style.css").toExternalForm());
      // Copy light-mode class if present
      if (currentScene.getRoot().getStyleClass().contains("light-mode")) {
        newScene.getRoot().getStyleClass().add("light-mode");
      }
      stage.setScene(newScene);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void switchToLogin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/login.fxml"));
      Parent loginRoot = loader.load();
      com.laplateforme.gui.MainApp.setRoot(loginRoot);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void fetchAndDisplayStats() {
    if (token == null || token.isEmpty())
      return;
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/students"))
        .header("Authorization", "Bearer " + token)
        .GET()
        .build();
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(this::updateAnalytics)
        .exceptionally(e -> {
          e.printStackTrace();
          return null;
        });
  }

  private void updateAnalytics(String json) {
    Gson gson = new Gson();
    List<Student> students = gson.fromJson(json, new TypeToken<List<Student>>() {
    }.getType());
    int total = students.size();
    double avg = students.stream().mapToDouble(Student::getGrade).average().orElse(0.0);
    long above = students.stream().filter(s -> s.getGrade() > avg).count();
    long equal = students.stream().filter(s -> s.getGrade() == avg).count();
    long below = students.stream().filter(s -> s.getGrade() < avg).count();
    javafx.application.Platform.runLater(() -> {
      totalStudentsLabel.setText("Total students: " + total);
      averageGradeLabel.setText(String.format("Average grade: %.2f", avg));
      // PieChart
      gradePieChart.setTitle("Grade Distribution");
      gradePieChart.getStyleClass().add("chart-title");
      gradePieChart.getData().clear();
      PieChart.Data aboveData = new PieChart.Data("> Avg", above);
      PieChart.Data equalData = new PieChart.Data("= Avg", equal);
      PieChart.Data belowData = new PieChart.Data("< Avg", below);
      gradePieChart.getData().addAll(aboveData, equalData, belowData);
      // Style pie labels
      for (PieChart.Data data : gradePieChart.getData()) {
        data.getNode().getStyleClass().add("chart-pie-label");
      }
      // Style legend
      if (gradePieChart.lookup(".chart-legend") != null) {
        gradePieChart.lookup(".chart-legend").getStyleClass().add("chart-legend");
      }
      // BarChart
      gradeBarChart.setTitle("Above / Equal / Below Avg");
      gradeBarChart.getStyleClass().add("chart-title");
      gradeBarChart.getData().clear();
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      XYChart.Data<String, Number> aboveBar = new XYChart.Data<>("> Avg", above);
      XYChart.Data<String, Number> equalBar = new XYChart.Data<>("= Avg", equal);
      XYChart.Data<String, Number> belowBar = new XYChart.Data<>("< Avg", below);
      series.getData().addAll(aboveBar, equalBar, belowBar);
      gradeBarChart.getData().add(series);
      gradeBarChart.setLegendVisible(false);
      // Style bar colors
      javafx.application.Platform.runLater(() -> {
        if (aboveBar.getNode() != null)
          aboveBar.getNode().setStyle("-fx-bar-fill: #1976D2;"); // blue
        if (equalBar.getNode() != null)
          equalBar.getNode().setStyle("-fx-bar-fill: #FFA726;"); // orange
        if (belowBar.getNode() != null)
          belowBar.getNode().setStyle("-fx-bar-fill: #43A047;"); // green
      });
      // Style legend
      if (gradeBarChart.lookup(".chart-legend") != null) {
        gradeBarChart.lookup(".chart-legend").getStyleClass().add("chart-legend");
      }
      // Style axis tick labels
      if (barCategoryAxis.lookupAll(".axis-tick-mark") != null) {
        barCategoryAxis.lookupAll(".axis-tick-mark").forEach(n -> n.getStyleClass().add("axis-tick-mark"));
      }
      if (barNumberAxis.lookupAll(".axis-tick-mark") != null) {
        barNumberAxis.lookupAll(".axis-tick-mark").forEach(n -> n.getStyleClass().add("axis-tick-mark"));
      }
    });
  }
}