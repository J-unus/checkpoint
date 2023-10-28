package ee.mustamae.checkpoint.controller;

import ee.mustamae.checkpoint.dto.ChatRoomCreateDto;
import ee.mustamae.checkpoint.dto.ChatRoomDto;
import ee.mustamae.checkpoint.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4209")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping
  public ChatRoomDto createChatRoom(@Valid @RequestBody ChatRoomCreateDto chatRoomCreateDto) {
    return chatRoomService.create(chatRoomCreateDto);
  }
}
