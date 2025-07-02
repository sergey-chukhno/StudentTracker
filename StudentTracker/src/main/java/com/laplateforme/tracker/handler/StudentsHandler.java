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
      if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
        StudentService studentService = new StudentService();
        List<Student> students = studentService.getAllStudents();
        Gson gson = new Gson();
        String json = gson.toJson(students);
        byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
          os.write(responseBytes);
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