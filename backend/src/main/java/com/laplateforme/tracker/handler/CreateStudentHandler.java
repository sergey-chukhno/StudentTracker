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
import com.laplateforme.tracker.util.AuthHelper;
import com.auth0.jwt.interfaces.DecodedJWT;

public class CreateStudentHandler implements HttpHandler {
  private final StudentService studentService = new StudentService();
  private final Gson gson = new Gson();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    DecodedJWT jwt = AuthHelper.requireAuth(exchange);
    if (jwt == null)
      return;
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1); // Method Not Allowed
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
    Student created = studentService.createStudent(student);
    String response = gson.toJson(created);
    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
    exchange.sendResponseHeaders(201, responseBytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(responseBytes);
    }
  }
}