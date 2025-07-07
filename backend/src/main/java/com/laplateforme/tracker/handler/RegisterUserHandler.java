package com.laplateforme.tracker.handler;

import com.laplateforme.tracker.service.UserService;
import com.laplateforme.tracker.model.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterUserHandler implements HttpHandler {
  private final UserService userService = new UserService();
  private final Gson gson = new Gson();

  static class RegisterRequest {
    String username;
    String password;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }
    RegisterRequest req;
    try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
      req = gson.fromJson(reader, RegisterRequest.class);
    } catch (Exception e) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    if (req == null || req.username == null || req.password == null || req.username.isEmpty()
        || req.password.isEmpty()) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    User user = userService.registerUser(req.username, req.password);
    if (user == null) {
      // Username already exists
      exchange.sendResponseHeaders(409, -1);
      return;
    }
    // Success: return user info (excluding password)
    Map<String, Object> resp = new HashMap<>();
    resp.put("id", user.getId());
    resp.put("username", user.getUsername());
    String json = gson.toJson(resp);
    exchange.getResponseHeaders().set("Content-Type", "application/json");
    exchange.sendResponseHeaders(201, json.getBytes(StandardCharsets.UTF_8).length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(json.getBytes(StandardCharsets.UTF_8));
    }
  }
}