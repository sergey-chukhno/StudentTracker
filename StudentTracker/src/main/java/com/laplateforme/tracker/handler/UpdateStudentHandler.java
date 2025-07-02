package com.laplateforme.tracker.handler;

import com.laplateforme.tracker.service.StudentService;
import com.laplateforme.tracker.model.Student;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateStudentHandler implements HttpHandler {
  private final StudentService studentService = new StudentService();
  private final Gson gson = new Gson();
  private static final Pattern ID_PATTERN = Pattern.compile("^/students/(\\d+)(/)?$");

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!"PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1); // Method Not Allowed
      return;
    }
    String path = exchange.getRequestURI().getPath();
    Matcher matcher = ID_PATTERN.matcher(path);
    if (!matcher.matches()) {
      exchange.sendResponseHeaders(404, -1);
      return;
    }
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
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    }
    String body = sb.toString();
    Student student;
    try {
      student = gson.fromJson(body, Student.class);
    } catch (Exception e) {
      String response = "Invalid JSON";
      byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(400, responseBytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(responseBytes);
      }
      return;
    }
    if (student.getFirstName() == null || student.getLastName() == null || student.getAge() <= 0
        || student.getGrade() < 0) {
      String response = "Missing or invalid student fields";
      byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(400, responseBytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(responseBytes);
      }
      return;
    }
    Student updated = studentService.updateStudent(id, student);
    if (updated == null) {
      String response = "Student not found";
      byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(404, responseBytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(responseBytes);
      }
      return;
    }
    String response = gson.toJson(updated);
    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
    exchange.sendResponseHeaders(200, responseBytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(responseBytes);
    }
  }
}