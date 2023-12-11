package ee.mustamae.checkpoint.controller;

import ee.mustamae.checkpoint.dto.ChatRoomAuthorizeDto;
import ee.mustamae.checkpoint.dto.ChatRoomCreateDto;
import ee.mustamae.checkpoint.dto.ChatRoomDto;
import ee.mustamae.checkpoint.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping
  public ChatRoomDto createChatRoom(@Valid @RequestBody ChatRoomCreateDto chatRoomCreateDto) {
    return chatRoomService.create(chatRoomCreateDto);
  }

  @PostMapping("/authorize")
  public String authorizeForChatRoom(@Valid @RequestBody ChatRoomAuthorizeDto chatRoomAuthorizeDto) {
    return chatRoomService.authorizeForChatRoom(chatRoomAuthorizeDto);
  }
}
