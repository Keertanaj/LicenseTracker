package org.licensetracker.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

  @Value("${springsecuritybasic.jwtSecret}")
  private String jwtSecret;

  @Value("${springsecuritybasic.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtTokenWithRole(String email, String role) {
    return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (Exception e) {
      log.error("JWT validation error: {}", e.getMessage());
    }
    return false;
  }

  public String getRoleFromJwtToken(String token) {
    return (String) Jwts.parser().setSigningKey(jwtSecret)
            .parseClaimsJws(token).getBody().get("role");
  }
}
