package ee.mustamae.checkpoint.interceptor;

import ee.mustamae.checkpoint.excpetion.WsDestinationUuidNotValidException;
import ee.mustamae.checkpoint.util.JwtTokenUtil;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

  private static final int UUID_LENGTH = 36;

  private final ChatRoomRepository chatRoomRepository;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = null;
    try {
      accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
      trySetAuthentication(accessor);
    } catch (Exception e) {
      log.error(e.getMessage());
      invalidateSession(accessor);
    }
    return message;
  }

  private void trySetAuthentication(StompHeaderAccessor accessor) {
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      handleConnect(accessor);
    } else if (!StompCommand.DISCONNECT.equals(accessor.getCommand()) && !StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())){
      validateSession(accessor);
    }
  }

  private void handleConnect(StompHeaderAccessor accessor) {
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

  private void validateSession(StompHeaderAccessor accessor) {
    String destination = accessor.getDestination();
    String destinationUuid = destination.split("/")[3];
    String authenticatedUuid = accessor.getUser().getName();

    if (UUID_LENGTH != destinationUuid.length() || !destinationUuid.equals(authenticatedUuid)) {
      throw new WsDestinationUuidNotValidException("Destination chat room uuid does not match with authenticated uuid");
    }
  }


  private void invalidateSession(StompHeaderAccessor accessor) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(null);

    if (accessor != null) {
      accessor.setUser(null);
    }
  }
}
