package com.laplateforme.tracker.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.service.StudentService;
import java.util.ArrayList;
import java.util.List;

public class ImportCsvHandler implements HttpHandler {
    private final StudentService studentService = new StudentService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
            if (contentType == null || !contentType.contains("multipart/form-data")) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }
            String boundary = null;
            for (String param : contentType.split(";")) {
                param = param.trim();
                if (param.startsWith("boundary=")) {
                    boundary = param.substring("boundary=".length());
                }
            }
            if (boundary == null) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }
            boundary = "--" + boundary;
            InputStream is = exchange.getRequestBody();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            is.close();
            String body = baos.toString(StandardCharsets.ISO_8859_1);
            // Find the file part
            int fileStart = body.indexOf("\r\n\r\n");
            if (fileStart == -1) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }
            fileStart += 4;
            int fileEnd = body.indexOf(boundary, fileStart) - 4;
            if (fileEnd < fileStart) fileEnd = body.length();
            String fileContent = body.substring(fileStart, fileEnd);
            // Parse CSV
            String[] lines = fileContent.split("\r?\n");
            int imported = 0;
            List<String> errors = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty() || line.toLowerCase().startsWith("firstname")) continue; // skip header/empty
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    errors.add("Line " + (i+1) + ": not enough columns");
                    continue;
                }
                try {
                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    double grade = Double.parseDouble(parts[3].trim());
                    Student student = new Student(0, firstName, lastName, age, grade);
                    studentService.createStudent(student);
                    imported++;
                } catch (Exception ex) {
                    errors.add("Line " + (i+1) + ": " + ex.getMessage());
                }
            }
            String response = "Imported " + imported + " students.";
            if (!errors.isEmpty()) {
                response += " Errors: " + String.join("; ", errors);
            }
            exchange.sendResponseHeaders(errors.isEmpty() ? 200 : 400, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
} 