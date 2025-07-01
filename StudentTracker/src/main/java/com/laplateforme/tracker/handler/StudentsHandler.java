package com.laplateforme.tracker.handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class StudentsHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String response = "Student API endpoint is running.";
    exchange.sendResponseHeaders(200, response.getBytes().length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(response.getBytes());
    }
  }
}