package com.laplateforme.tracker;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.laplateforme.tracker.handler.GetAllStudentsHandler;
import com.laplateforme.tracker.handler.GetStudentByIdHandler;
import com.laplateforme.tracker.handler.CreateStudentHandler;
import com.laplateforme.tracker.handler.UpdateStudentHandler;
import com.laplateforme.tracker.handler.DeleteStudentHandler;
import com.laplateforme.tracker.handler.RegisterUserHandler;
import com.laplateforme.tracker.handler.LoginHandler;
import com.laplateforme.tracker.util.DatabaseManager;

public class StudentTrackerServer {
  public static void main(String[] args) throws IOException {
    // Ensure DB connection and table creation on startup
    DatabaseManager.getConnection();
    int port = 8080;
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/students", exchange -> {
      String method = exchange.getRequestMethod();
      String path = exchange.getRequestURI().getPath();
      if ("GET".equalsIgnoreCase(method) && "/students".equals(path)) {
        new GetAllStudentsHandler().handle(exchange);
      } else if ("POST".equalsIgnoreCase(method) && "/students".equals(path)) {
        new CreateStudentHandler().handle(exchange);
      } else if ("GET".equalsIgnoreCase(method) && path.matches("^/students/\\d+/?$")) {
        new GetStudentByIdHandler().handle(exchange);
      } else if ("PUT".equalsIgnoreCase(method) && path.matches("^/students/\\d+/?$")) {
        new UpdateStudentHandler().handle(exchange);
      } else if ("DELETE".equalsIgnoreCase(method) && path.matches("^/students/\\d+/?$")) {
        new DeleteStudentHandler().handle(exchange);
      } else {
        exchange.sendResponseHeaders(404, -1);
      }
    });
    server.createContext("/register", new RegisterUserHandler());
    server.createContext("/login", new LoginHandler());
    server.setExecutor(null); // creates a default executor
    System.out.println("[INFO] HTTP server started on port " + port);
    server.start();
  }
}