package ee.mustamae.checkpoint.repository;

import ee.mustamae.checkpoint.model.AuthenticatedChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticatedChatSessionRepository extends JpaRepository<AuthenticatedChatSession, Long> {
}
