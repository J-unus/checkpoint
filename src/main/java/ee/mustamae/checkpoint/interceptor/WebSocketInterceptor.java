package ee.mustamae.checkpoint.interceptor;

import ee.mustamae.checkpoint.config.JwtTokenUtil;
import ee.mustamae.checkpoint.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

  private final ChatRoomRepository chatRoomRepository;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    try {
      trySetAuthentication(message);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return message;
  }

  private void trySetAuthentication(Message<?> message) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String authorizationHeader = accessor.getNativeHeader("Authorization").get(0);

      if (!StringUtils.hasText(authorizationHeader) && !authorizationHeader.startsWith("Bearer ")) {
        return;
      }

      String token = authorizationHeader.substring(7);
      String chatRoomUuid = JwtTokenUtil.extractChatRoomUuid(token);

      if (chatRoomUuid == null || SecurityContextHolder.getContext().getAuthentication() != null) {
        return;
      }

      boolean existsByUuid = chatRoomRepository.existsByUuid(chatRoomUuid);
      if (JwtTokenUtil.validateToken(token, existsByUuid)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(chatRoomUuid, null, new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        accessor.setUser(authentication);
      }
    }
  }
}
