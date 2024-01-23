package ee.mustamae.checkpoint.service;

import ee.mustamae.checkpoint.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
  private static final long ONE_MINUTE_IN_MS = 60 * 1000;

  private final SecurityProperties securityProperties;

  public String generateToken(String userName) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userName);
  }

  private String createToken(Map<String, Object> claims, String userName) {
    return Jwts.builder()
      .claims(claims)
      .subject(userName)
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + securityProperties.getJwtExpirationTimeMinutes() * ONE_MINUTE_IN_MS))
      .signWith(getSignKey())
      .compact();
  }

  private SecretKey getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(securityProperties.getJwtSignatureSecretBase64Key());
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractChatRoomUuid(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parser()
      .verifyWith(getSignKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, boolean existsByUuid) {
    return existsByUuid && !isTokenExpired(token);
  }
}
