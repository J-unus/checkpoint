package ee.mustamae.checkpoint.controller;

import ee.mustamae.checkpoint.dto.ChatRoomAuthorizeDto;
import ee.mustamae.checkpoint.dto.ChatRoomCreateDto;
import ee.mustamae.checkpoint.dto.ChatRoomDto;
import ee.mustamae.checkpoint.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final AuthenticationManager authenticationManager;

  @PostMapping
  public ChatRoomDto createChatRoom(@Valid @RequestBody ChatRoomCreateDto chatRoomCreateDto) {
    return chatRoomService.create(chatRoomCreateDto);
  }

  @PostMapping("/authorize")
  public void authorizeForChatRoom(@Valid @RequestBody ChatRoomAuthorizeDto chatRoomAuthorizeDto, HttpServletRequest request, HttpServletResponse response) {
    UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(chatRoomAuthorizeDto.getUuid(), chatRoomAuthorizeDto.getPassword());
    Authentication authentication = authenticationManager.authenticate(token);
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    System.out.println(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
  }
}
