package com.laplateforme.tracker.util;

import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;

public class JwtUtil {
  private static final String ENV_SECRET_NAME = "JWT_SECRET";
  private static final String DEFAULT_SECRET = "supersecretkey";
  private static final Algorithm algorithm;
  static {
    // Load .env file if present
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    String secret = dotenv.get(ENV_SECRET_NAME);
    if (secret == null || secret.isEmpty()) {
      secret = System.getenv(ENV_SECRET_NAME);
    }
    if (secret == null || secret.isEmpty()) {
      System.err.println(
          "[WARN] JWT_SECRET environment variable not set. Using default secret. DO NOT USE DEFAULT IN PRODUCTION!");
      secret = DEFAULT_SECRET;
    }
    algorithm = Algorithm.HMAC256(secret);
  }

  public static Algorithm getAlgorithm() {
    return algorithm;
  }

  public static DecodedJWT verifyToken(String token) {
    try {
      JWTVerifier verifier = JWT.require(getAlgorithm()).build();
      return verifier.verify(token);
    } catch (JWTVerificationException e) {
      return null;
    }
  }
}