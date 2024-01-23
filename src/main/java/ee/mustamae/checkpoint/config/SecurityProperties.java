package ee.mustamae.checkpoint.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.security", ignoreUnknownFields = false)
@Getter
@Setter
public class SecurityProperties {
  private List<String> allowedOrigins;
  private String jwtSignatureSecretBase64Key;
  private long jwtExpirationTimeMinutes;
}
