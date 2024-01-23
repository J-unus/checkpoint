package ee.mustamae.checkpoint.service;

import ee.mustamae.checkpoint.model.ChatRoom;
import ee.mustamae.checkpoint.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Configurable
@RequiredArgsConstructor
public class ChatRoomAuthenticationProvider implements AuthenticationProvider {

  private final ChatRoomRepository chatRoomRepository;
  private final PasswordEncoder delegatingPasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String chatRoomUuid = authentication.getPrincipal() + "";
    String password = authentication.getCredentials() + "";

    if (!StringUtils.hasText(chatRoomUuid) || !StringUtils.hasText(password)) {
      return null;
    }

    Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByUuid(chatRoomUuid);
    if (optionalChatRoom.isEmpty() || !delegatingPasswordEncoder.matches(password, optionalChatRoom.get().getPassword())) {
      return null;
    }

    List<GrantedAuthority> grantedAuths = new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    return UsernamePasswordAuthenticationToken.authenticated(chatRoomUuid, null, grantedAuths);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
