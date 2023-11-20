package ee.mustamae.checkpoint.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "authenticated_chat_session")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AuthenticatedChatSession extends BaseModel {

  @Column(nullable = false)
  private String chatRoomUuid;

  @Column(nullable = false)
  private String simpSessionId;
}
