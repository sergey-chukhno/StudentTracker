package com.laplateforme.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.laplateforme.gui.Student;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

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
    fetchAndPopulateStudents();
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
  private TableView<Student> studentTable;
  @FXML
  private TableColumn<Student, String> firstNameColumn;
  @FXML
  private TableColumn<Student, String> lastNameColumn;
  @FXML
  private TableColumn<Student, Integer> ageColumn;
  @FXML
  private TableColumn<Student, Double> gradeColumn;
  @FXML
  private TableColumn<Student, Void> actionsColumn;

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
    if (studentTable != null) {
      setupStudentTable();
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

  private void setupStudentTable() {
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
    gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    // Actions column setup
    actionsColumn.setCellFactory(getActionsCellFactory());
    // Ensure only intended columns are shown
    studentTable.getColumns().setAll(firstNameColumn, lastNameColumn, ageColumn, gradeColumn, actionsColumn);
  }

  private Callback<TableColumn<Student, Void>, TableCell<Student, Void>> getActionsCellFactory() {
    return new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
      @Override
      public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
        return new TableCell<Student, Void>() {
          private final Button updateBtn = new Button("Update");
          private final Button deleteBtn = new Button("Delete");
          {
            updateBtn.getStyleClass().addAll("student-action-btn", "student-update-btn");
            deleteBtn.getStyleClass().addAll("student-action-btn", "student-delete-btn");
            updateBtn.setOnAction(e -> {
              Student student = getTableView().getItems().get(getIndex());
              // TODO: handle update
            });
            deleteBtn.setOnAction(e -> {
              Student student = getTableView().getItems().get(getIndex());
              // TODO: handle delete
            });
          }

          @Override
          public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
            } else {
              HBox box = new HBox(10, updateBtn, deleteBtn);
              box.setAlignment(Pos.CENTER_LEFT);
              setGraphic(box);
            }
          }
        };
      }
    };
  }

  private void fetchAndPopulateStudents() {
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
        .thenAccept(this::updateStudentTableFromJson)
        .exceptionally(e -> {
          e.printStackTrace();
          return null;
        });
  }

  private void updateStudentTableFromJson(String json) {
    Gson gson = new Gson();
    List<Student> students = gson.fromJson(json, new TypeToken<List<Student>>() {
    }.getType());
    ObservableList<Student> data = FXCollections.observableArrayList(students);
    javafx.application.Platform.runLater(() -> studentTable.setItems(data));
  }
}