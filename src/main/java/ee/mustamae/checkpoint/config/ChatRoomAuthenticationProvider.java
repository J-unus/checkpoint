package ee.mustamae.checkpoint.config;

import ee.mustamae.checkpoint.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Configurable
@RequiredArgsConstructor
public class ChatRoomAuthenticationProvider implements AuthenticationProvider {

  private final ChatRoomRepository chatRoomRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String chatRoomUuid = authentication.getPrincipal() + "";
    String password = authentication.getCredentials() + "";

//    if (!chatRoomRepository.existsByUuidAndPassword(chatRoomUuid, password)) {
//      throw new RuntimeException();
//    }

    List<GrantedAuthority> grantedAuths = new ArrayList<>();
    grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
    return UsernamePasswordAuthenticationToken.authenticated(chatRoomUuid, null, grantedAuths);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
