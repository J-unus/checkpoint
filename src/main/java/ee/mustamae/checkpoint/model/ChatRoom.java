package ee.mustamae.checkpoint.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "chatRooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ChatRoom extends BaseModel {

  @Indexed
  private String uuid;

  private String password;
}
