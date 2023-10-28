package ee.mustamae.checkpoint.controller;

import ee.mustamae.checkpoint.dto.MessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageWsController {

  @MessageMapping("/checkpoint")
  @SendTo("/topic/checkpoint")
  public MessageDto checkpoint(String message) {
    return new MessageDto("Received, " + HtmlUtils.htmlEscape(message) + "!");
  }
}
