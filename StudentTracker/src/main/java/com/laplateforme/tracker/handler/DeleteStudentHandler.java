package com.laplateforme.tracker.handler;

import com.laplateforme.tracker.service.StudentService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteStudentHandler implements HttpHandler {
  private final StudentService studentService = new StudentService();
  private static final Pattern ID_PATTERN = Pattern.compile("^/students/(\\d+)(/)?$");

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!"DELETE".equalsIgnoreCase(exchange.getRequestMethod())) {
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
    boolean deleted = studentService.deleteStudent(id);
    if (!deleted) {
      String response = "Student not found";
      byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(404, responseBytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(responseBytes);
      }
      return;
    }
    exchange.sendResponseHeaders(204, -1); // No Content
  }
}