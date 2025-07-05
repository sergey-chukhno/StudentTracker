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
import javafx.collections.transformation.FilteredList;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.stage.FileChooser;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
  private Button prevPageButton;
  @FXML
  private Button nextPageButton;
  @FXML
  private Label pageIndicator;

  private int currentPage = 1;
  private int pageSize = 10;
  private int totalPages = 1;

  private FilteredList<Student> filteredStudents;
  private String currentSearch = "";
  private SortedList<Student> sortedStudents;
  private ObservableList<Student> allStudents = FXCollections.observableArrayList();

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
    if (exportPdfButton != null) {
      exportPdfButton.setOnAction(e -> exportToPdf());
    }
    if (dashboardButton != null) {
      dashboardButton.setOnAction(e -> switchToDashboard());
    }
    if (analyticsButton != null) {
      analyticsButton.setOnAction(e -> switchToAnalytics());
    }
    if (exitButton != null) {
      exitButton.setOnAction(e -> System.exit(0));
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
              showDeleteStudentDialog(student);
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
    allStudents.setAll(students);
    filteredStudents = new FilteredList<>(allStudents, s -> true);
    sortedStudents = new SortedList<>(filteredStudents);
    javafx.application.Platform.runLater(() -> {
      setupPagination();
      sortedStudents.comparatorProperty().bind(studentTable.comparatorProperty());
      studentTable.setItems(sortedStudents);
    });
    setupSearchFilter();
  }

  private void setupPagination() {
    if (paginationCombo != null) {
      paginationCombo.getItems().setAll(10, 25, 50);
      paginationCombo.setValue(pageSize);
      paginationCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
        pageSize = newVal;
        currentPage = 1;
        updatePaginationPredicate();
      });
    }
    if (prevPageButton != null) {
      prevPageButton.setOnAction(e -> {
        if (currentPage > 1) {
          currentPage--;
          updatePaginationPredicate();
        }
      });
    }
    if (nextPageButton != null) {
      nextPageButton.setOnAction(e -> {
        if (currentPage < totalPages) {
          currentPage++;
          updatePaginationPredicate();
        }
      });
    }
    updatePaginationPredicate();
  }

  private void updatePaginationPredicate() {
    // First, filter by search
    List<Student> searchFiltered = allStudents
        .filtered(s -> (s.getFirstName() != null && s.getFirstName().toLowerCase().contains(currentSearch)) ||
            (s.getLastName() != null && s.getLastName().toLowerCase().contains(currentSearch)) ||
            String.valueOf(s.getAge()).contains(currentSearch) ||
            String.valueOf(s.getGrade()).contains(currentSearch));
    int totalItems = searchFiltered.size();
    totalPages = (int) Math.ceil((double) totalItems / pageSize);
    if (totalPages == 0)
      totalPages = 1;
    if (currentPage > totalPages)
      currentPage = totalPages;
    if (pageIndicator != null) {
      pageIndicator.setText("Page " + currentPage + " of " + totalPages);
    }
    if (prevPageButton != null)
      prevPageButton.setDisable(currentPage == 1);
    if (nextPageButton != null)
      nextPageButton.setDisable(currentPage == totalPages);
    int fromIndex = (currentPage - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, totalItems);
    filteredStudents.setPredicate(s -> {
      int idx = searchFiltered.indexOf(s);
      return idx >= fromIndex && idx < toIndex;
    });
  }

  private void setupSearchFilter() {
    if (searchField == null)
      return;
    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
      currentSearch = newVal.toLowerCase().trim();
      currentPage = 1;
      updatePaginationPredicate();
    });
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

  private void showDeleteStudentDialog(Student student) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Delete Student");
    alert.setHeaderText(null);
    alert.setContentText(
        "Are you sure you want to delete " + student.getFirstName() + " " + student.getLastName() + "?");
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStyleClass().add("custom-dialog");
    dialogPane.getStylesheets().add(addStudentButton.getScene().getStylesheets().get(0));
    dialogPane.getStyleClass().addAll(addStudentButton.getScene().getRoot().getStyleClass());
    Stage stage = (Stage) addStudentButton.getScene().getWindow();
    alert.initOwner(stage);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      deleteStudentInBackend(student);
    }
  }

  private void deleteStudentInBackend(Student student) {
    if (token == null || token.isEmpty())
      return;
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/students/" + student.getId()))
        .header("Authorization", "Bearer " + token)
        .DELETE()
        .build();
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenAccept(response -> {
          if (response.statusCode() == 204) {
            fetchAndPopulateStudents();
          } else {
            javafx.application.Platform
                .runLater(() -> showAlert("Failed to delete student. Status: " + response.statusCode()));
          }
        })
        .exceptionally(e -> {
          e.printStackTrace();
          javafx.application.Platform.runLater(() -> showAlert("Network error: " + e.getMessage()));
          return null;
        });
  }

  private void exportToPdf() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save PDF Report");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
    fileChooser.setInitialFileName("student_report.pdf");
    File file = fileChooser.showSaveDialog(exportPdfButton.getScene().getWindow());
    if (file == null)
      return;
    try {
      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();
      document.addTitle("Student Dashboard Report");
      document.add(new Paragraph("Student Dashboard Report"));
      document.add(new Paragraph(
          "Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
      document.add(new Paragraph(" "));
      PdfPTable table = new PdfPTable(4);
      table.setWidthPercentage(100);
      table.addCell("First Name");
      table.addCell("Last Name");
      table.addCell("Age");
      table.addCell("Grade");
      // Export all students in current sorted and filtered order
      for (Student student : sortedStudents) {
        table.addCell(student.getFirstName());
        table.addCell(student.getLastName());
        table.addCell(String.valueOf(student.getAge()));
        table.addCell(String.valueOf(student.getGrade()));
      }
      document.add(table);
      document.close();
    } catch (DocumentException | IOException ex) {
      showAlert("Failed to export PDF: " + ex.getMessage());
    }
  }

  private void switchToDashboard() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/dashboard.fxml"));
      Parent dashboardRoot = loader.load();
      com.laplateforme.gui.controller.MainController dashboardController = loader.getController();
      dashboardController.setUser(username, token);
      Stage stage = (Stage) dashboardButton.getScene().getWindow();
      Scene currentScene = dashboardButton.getScene();
      Scene newScene = new Scene(dashboardRoot);
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

  private void switchToAnalytics() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laplateforme/gui/fxml/analytics.fxml"));
      Parent analyticsRoot = loader.load();
      com.laplateforme.gui.controller.AnalyticsController analyticsController = loader.getController();
      analyticsController.setUser(username, token);
      Stage stage = (Stage) analyticsButton.getScene().getWindow();
      Scene currentScene = analyticsButton.getScene();
      Scene newScene = new Scene(analyticsRoot);
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
}