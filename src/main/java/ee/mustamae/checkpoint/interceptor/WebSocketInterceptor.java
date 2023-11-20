package ee.mustamae.checkpoint.interceptor;

import ee.mustamae.checkpoint.repository.ChatRoomRepository;
import ee.mustamae.checkpoint.service.AuthenticatedChatSessionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor  {

  private final ChatRoomRepository chatRoomRepository;
  private final AuthenticationManager authenticationManager;

  @SneakyThrows
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String password = accessor.getNativeHeader("password").get(0);
      String chatRoomUuid = accessor.getNativeHeader("chatRoomUuid").get(0);
      String simpSessionId = (String) accessor.getHeader("simpSessionId");
      if (StringUtils.hasText(password)) {
        if (chatRoomRepository.existsByUuidAndPassword(chatRoomUuid, password)) {
          UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(chatRoomUuid, password);
          Authentication authentication = authenticationManager.authenticate(token);
          SecurityContext context = SecurityContextHolder.getContext();
          context.setAuthentication(authentication);
          accessor.setUser(authentication);
        } else {
          throw new AuthenticationException("error");
        }
      } else {
        throw new AuthenticationException("error");
      }
    }
    System.out.println(message);
    System.out.println(accessor.getCommand());

    return message;
  }
}
