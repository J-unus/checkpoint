package ee.mustamae.checkpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
  private String id;
  private Instant createdAt;
  private String body;
}
