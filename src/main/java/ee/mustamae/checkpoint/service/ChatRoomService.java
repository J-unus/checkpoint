package ee.mustamae.checkpoint.service;

import ee.mustamae.checkpoint.dto.ChatRoomAuthorizeDto;
import ee.mustamae.checkpoint.dto.ChatRoomCreateDto;
import ee.mustamae.checkpoint.dto.ChatRoomDto;
import ee.mustamae.checkpoint.mapper.ChatRoomMapper;
import ee.mustamae.checkpoint.model.ChatRoom;
import ee.mustamae.checkpoint.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatRoomMapper chatRoomMapper;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  public ChatRoomDto create(ChatRoomCreateDto chatRoomCreateDto) {
    ChatRoom chatRoom = new ChatRoom();
    chatRoom.setUuid(UUID.randomUUID().toString());
    chatRoom.setPassword(chatRoomCreateDto.getPassword());
    return chatRoomMapper.fromEntityToDto(chatRoomRepository.save(chatRoom));
  }

  public String authorizeForChatRoom(ChatRoomAuthorizeDto chatRoomAuthorizeDto) {
    UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(chatRoomAuthorizeDto.getUuid(), chatRoomAuthorizeDto.getPassword());
    Authentication authentication = authenticationManager.authenticate(token);
    if (authentication.isAuthenticated()) {
      return jwtTokenService.generateToken(chatRoomAuthorizeDto.getUuid());
    } else {
      return null;
    }
  }
}
