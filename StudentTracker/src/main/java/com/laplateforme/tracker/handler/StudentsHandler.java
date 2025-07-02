package com.laplateforme.tracker.handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import com.laplateforme.tracker.service.StudentService;
import com.google.gson.Gson;
import com.laplateforme.tracker.model.Student;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class StudentsHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      String path = exchange.getRequestURI().getPath();
      String method = exchange.getRequestMethod();
      StudentService studentService = new StudentService();
      Gson gson = new Gson();
      if ("GET".equalsIgnoreCase(method)) {
        // Use regex to match /students or /students/{id}
        String studentsPattern = "^/students/?$";
        String studentByIdPattern = "^/students/(\\d+)(/)?$";
        java.util.regex.Pattern idPattern = java.util.regex.Pattern.compile(studentByIdPattern);
        java.util.regex.Matcher matcher = idPattern.matcher(path);
        if (matcher.matches()) {
          // GET /students/{id} (with or without trailing slash)
          int id;
          try {
            id = Integer.parseInt(matcher.group(1));
          } catch (Exception e) {
            String response = "Invalid student ID";
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(400, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
              os.write(responseBytes);
            }
            return;
          }
          Student student = studentService.getStudentById(id);
          if (student == null) {
            String response = "Student not found";
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
              os.write(responseBytes);
            }
            return;
          }
          String json = gson.toJson(student);
          byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
          exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
          exchange.sendResponseHeaders(200, responseBytes.length);
          try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
          }
        } else if (path.matches(studentsPattern)) {
          // GET /students (with or without trailing slash)
          List<Student> students = studentService.getAllStudents();
          String json = gson.toJson(students);
          byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
          exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
          exchange.sendResponseHeaders(200, responseBytes.length);
          try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
          }
        } else {
          String response = "Not Found";
          byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
          exchange.sendResponseHeaders(404, responseBytes.length);
          try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
          }
        }
      } else {
        String response = "Method Not Allowed";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(405, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
          os.write(responseBytes);
        }
      }
    } catch (Exception e) {
      String error = "Internal Server Error: " + e.getMessage();
      byte[] errorBytes = error.getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
      exchange.sendResponseHeaders(500, errorBytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(errorBytes);
      }
    }
  }
}