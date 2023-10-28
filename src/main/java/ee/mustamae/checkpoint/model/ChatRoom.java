package ee.mustamae.checkpoint.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "chat_room")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ChatRoom extends BaseModel {

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private String password;
}
