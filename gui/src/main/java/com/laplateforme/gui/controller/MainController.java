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
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import java.net.http.HttpRequest.BodyPublishers;
import javafx.stage.Stage;

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
    if (addStudentButton != null) {
      addStudentButton.setOnAction(e -> showAddStudentDialog());
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
              showUpdateStudentDialog(student);
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

  private void showAddStudentDialog() {
    Dialog<Student> dialog = new Dialog<>();
    dialog.setTitle("Add Student");
    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    TextField firstNameField = new TextField();
    TextField lastNameField = new TextField();
    TextField ageField = new TextField();
    TextField gradeField = new TextField();

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    dialogPane.getStyleClass().add("custom-dialog");
    Label firstNameLabel = new Label("First Name:");
    firstNameLabel.getStyleClass().add("custom-dialog-label");
    Label lastNameLabel = new Label("Last Name:");
    lastNameLabel.getStyleClass().add("custom-dialog-label");
    Label ageLabel = new Label("Age:");
    ageLabel.getStyleClass().add("custom-dialog-label");
    Label gradeLabel = new Label("Grade:");
    gradeLabel.getStyleClass().add("custom-dialog-label");
    grid.add(firstNameLabel, 0, 0);
    grid.add(firstNameField, 1, 0);
    grid.add(lastNameLabel, 0, 1);
    grid.add(lastNameField, 1, 1);
    grid.add(ageLabel, 0, 2);
    grid.add(ageField, 1, 2);
    grid.add(gradeLabel, 0, 3);
    grid.add(gradeField, 1, 3);
    dialogPane.setContent(grid);

    // Inherit root style class and stylesheet for correct theme
    dialogPane.getStylesheets().add(addStudentButton.getScene().getStylesheets().get(0));
    dialogPane.getStyleClass().addAll(addStudentButton.getScene().getRoot().getStyleClass());

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == ButtonType.OK) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gradeText = gradeField.getText().trim();
        if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty() || gradeText.isEmpty()) {
          showAlert("All fields are required.");
          return null;
        }
        int age;
        double grade;
        try {
          age = Integer.parseInt(ageText);
          grade = Double.parseDouble(gradeText);
        } catch (NumberFormatException e) {
          showAlert("Age must be an integer and grade must be a number.");
          return null;
        }
        return new Student(0, firstName, lastName, age, grade);
      }
      return null;
    });

    Stage stage = (Stage) addStudentButton.getScene().getWindow();
    dialog.initOwner(stage);
    Optional<Student> result = dialog.showAndWait();
    result.ifPresent(this::addStudentToBackend);
  }

  private void showUpdateStudentDialog(Student student) {
    Dialog<Student> dialog = new Dialog<>();
    dialog.setTitle("Update Student");
    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    TextField firstNameField = new TextField(student.getFirstName());
    TextField lastNameField = new TextField(student.getLastName());
    TextField ageField = new TextField(String.valueOf(student.getAge()));
    TextField gradeField = new TextField(String.valueOf(student.getGrade()));

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    dialogPane.getStyleClass().add("custom-dialog");
    Label firstNameLabel = new Label("First Name:");
    firstNameLabel.getStyleClass().add("custom-dialog-label");
    Label lastNameLabel = new Label("Last Name:");
    lastNameLabel.getStyleClass().add("custom-dialog-label");
    Label ageLabel = new Label("Age:");
    ageLabel.getStyleClass().add("custom-dialog-label");
    Label gradeLabel = new Label("Grade:");
    gradeLabel.getStyleClass().add("custom-dialog-label");
    grid.add(firstNameLabel, 0, 0);
    grid.add(firstNameField, 1, 0);
    grid.add(lastNameLabel, 0, 1);
    grid.add(lastNameField, 1, 1);
    grid.add(ageLabel, 0, 2);
    grid.add(ageField, 1, 2);
    grid.add(gradeLabel, 0, 3);
    grid.add(gradeField, 1, 3);
    dialogPane.setContent(grid);
    // Inherit root style class and stylesheet for correct theme
    dialogPane.getStylesheets().add(addStudentButton.getScene().getStylesheets().get(0));
    dialogPane.getStyleClass().addAll(addStudentButton.getScene().getRoot().getStyleClass());
    Stage stage = (Stage) addStudentButton.getScene().getWindow();
    dialog.initOwner(stage);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == ButtonType.OK) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gradeText = gradeField.getText().trim();
        if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty() || gradeText.isEmpty()) {
          showAlert("All fields are required.");
          return null;
        }
        int age;
        double grade;
        try {
          age = Integer.parseInt(ageText);
          grade = Double.parseDouble(gradeText);
        } catch (NumberFormatException e) {
          showAlert("Age must be an integer and grade must be a number.");
          return null;
        }
        return new Student(student.getId(), firstName, lastName, age, grade);
      }
      return null;
    });

    Optional<Student> result = dialog.showAndWait();
    result.ifPresent(this::updateStudentInBackend);
  }

  private void updateStudentInBackend(Student student) {
    if (token == null || token.isEmpty())
      return;
    Gson gson = new Gson();
    String json = gson.toJson(student);
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/students/" + student.getId()))
        .header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json")
        .PUT(BodyPublishers.ofString(json))
        .build();
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenAccept(response -> {
          if (response.statusCode() == 200) {
            fetchAndPopulateStudents();
          } else {
            javafx.application.Platform
                .runLater(() -> showAlert("Failed to update student. Status: " + response.statusCode()));
          }
        })
        .exceptionally(e -> {
          e.printStackTrace();
          javafx.application.Platform.runLater(() -> showAlert("Network error: " + e.getMessage()));
          return null;
        });
  }

  private void showAlert(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Input Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void addStudentToBackend(Student student) {
    if (token == null || token.isEmpty())
      return;
    Gson gson = new Gson();
    String json = gson.toJson(student);
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/students"))
        .header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json))
        .build();
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenAccept(response -> {
          if (response.statusCode() == 201) {
            // Refresh table
            fetchAndPopulateStudents();
          } else {
            javafx.application.Platform
                .runLater(() -> showAlert("Failed to add student. Status: " + response.statusCode()));
          }
        })
        .exceptionally(e -> {
          e.printStackTrace();
          javafx.application.Platform.runLater(() -> showAlert("Network error: " + e.getMessage()));
          return null;
        });
  }
}