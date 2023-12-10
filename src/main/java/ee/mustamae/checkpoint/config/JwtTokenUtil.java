package ee.mustamae.checkpoint.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class JwtTokenUtil {
  public final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
  public String generateToken(String userName) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userName);
  }

  private String createToken(Map<String, Object> claims, String userName) {
    return Jwts.builder()
      .claims(claims)
      .subject(userName)
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 300))
      .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // TODO replace deprecated
  }

  private Key getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET);
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
      .setSigningKey(getSignKey())
      .build()
      .parseClaimsJws(token)
      .getPayload();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, boolean existsByUuid) {
    return existsByUuid && !isTokenExpired(token);
  }
}
