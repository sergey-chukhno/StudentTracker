package com.laplateforme.tracker.handler;

import com.laplateforme.tracker.service.UserService;
import com.laplateforme.tracker.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {
  private final UserService userService = new UserService();
  private final Gson gson = new Gson();
  // In production, use a secure, private secret key loaded from config
  private static final String JWT_SECRET = "supersecretkey";
  private static final long EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000; // 24 hours

  static class LoginRequest {
    String username;
    String password;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }
    LoginRequest req;
    try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
      req = gson.fromJson(reader, LoginRequest.class);
    } catch (Exception e) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    if (req == null || req.username == null || req.password == null || req.username.isEmpty()
        || req.password.isEmpty()) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    User user = userService.getUserByUsername(req.username);
    if (user == null || !BCrypt.checkpw(req.password, user.getPasswordHash())) {
      exchange.sendResponseHeaders(401, -1);
      return;
    }
    // Generate JWT token
    Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
    String token = JWT.create()
        .withSubject(user.getUsername())
        .withClaim("id", user.getId())
        .withClaim("username", user.getUsername())
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
        .sign(algorithm);
    Map<String, String> resp = new HashMap<>();
    resp.put("token", token);
    String json = gson.toJson(resp);
    exchange.getResponseHeaders().set("Content-Type", "application/json");
    exchange.sendResponseHeaders(200, json.getBytes(StandardCharsets.UTF_8).length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(json.getBytes(StandardCharsets.UTF_8));
    }
  }
}