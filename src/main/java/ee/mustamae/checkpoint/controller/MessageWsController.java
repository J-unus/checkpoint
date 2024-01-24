package ee.mustamae.checkpoint.controller;

import ee.mustamae.checkpoint.dto.MessageDto;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class MessageWsController {

  @MessageMapping("/checkpoint/{chatRoomUuid}")
  @SendTo("/topic/checkpoint/{chatRoomUuid}")
  public MessageDto checkpoint(String message, @Header String userMessageId) {
    return new MessageDto(userMessageId, Instant.now(), message);
  }
}
