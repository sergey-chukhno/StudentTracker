package com.laplateforme.tracker.handler;

import com.laplateforme.tracker.service.StudentService;
import com.laplateforme.tracker.model.Student;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.laplateforme.tracker.util.AuthHelper;
import com.auth0.jwt.interfaces.DecodedJWT;

public class GetAllStudentsHandler implements HttpHandler {
    private final StudentService studentService = new StudentService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DecodedJWT jwt = AuthHelper.requireAuth(exchange);
        if (jwt == null)
            return;
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }
        List<Student> students = studentService.getAllStudents();
        String response = gson.toJson(students);
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}