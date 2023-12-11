package ee.mustamae.checkpoint.controller;

import ee.mustamae.checkpoint.dto.MessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageWsController {

  @MessageMapping("/checkpoint/{chatRoomUuid}")
  @SendTo("/topic/checkpoint/{chatRoomUuid}")
  public MessageDto checkpoint(String message) {
    return new MessageDto(message);
  }
}
