package ee.mustamae.checkpoint.repository;

import ee.mustamae.checkpoint.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, Long> {

  Optional<ChatRoom> findByUuid(String uuid);

  boolean existsByUuid(String uuid);
}
