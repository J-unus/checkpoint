package ee.mustamae.checkpoint.service;

import ee.mustamae.checkpoint.model.AuthenticatedChatSession;
import ee.mustamae.checkpoint.repository.AuthenticatedChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedChatSessionService {

  private final AuthenticatedChatSessionRepository authenticatedChatSessionRepository;

  public void createSession(String chatRoomUuid, String simpSessionId) {
    AuthenticatedChatSession chatRoom = AuthenticatedChatSession.builder()
      .chatRoomUuid(chatRoomUuid)
      .simpSessionId(simpSessionId)
      .build();
    authenticatedChatSessionRepository.save(chatRoom);
  }
}
