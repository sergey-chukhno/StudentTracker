package com.laplateforme.tracker.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class AuthHelper {
  public static DecodedJWT requireAuth(HttpExchange exchange) throws IOException {
    String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      exchange.sendResponseHeaders(401, -1);
      return null;
    }
    String token = authHeader.substring("Bearer ".length());
    DecodedJWT jwt = JwtUtil.verifyToken(token);
    if (jwt == null) {
      exchange.sendResponseHeaders(401, -1);
      return null;
    }
    return jwt;
  }
}