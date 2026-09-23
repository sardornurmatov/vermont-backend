package uz.vermont.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Date;

@Service
public class JwtService {
  private final SecretKey key;
  public JwtService(@Value("${app.jwt-secret}") String secret) {
    if (secret.length() < 32) throw new IllegalStateException("JWT_SECRET kamida 32 belgidan iborat bo‘lishi kerak");
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }
  public String create(String subject, String role, Duration lifetime) {
    Instant now = Instant.now();
    return Jwts.builder().subject(subject).claim("role", role).issuedAt(Date.from(now))
      .expiration(Date.from(now.plus(lifetime))).signWith(key).compact();
  }
  public Claims parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
