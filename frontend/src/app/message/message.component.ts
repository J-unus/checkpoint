import {Component, OnDestroy, OnInit} from '@angular/core';
import {WebSocketService} from "../web-socket/web-socket.service";
import {map, Subscription} from "rxjs";
import {MessageDto} from "./message.dto";

@Component({
  selector: 'checkpoint-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit, OnDestroy {
  private readonly TOPIC_ENDPOINT = '/topic/checkpoint';
  private readonly MESSAGE_DESTINATION = '/app/checkpoint';
  receivedMessages: MessageDto[] = [];
  private topicSubscription: Subscription;
  message: string;

  constructor(private webSocketService: WebSocketService) {
  }

  ngOnInit(): void {
    this.topicSubscription = this.webSocketService
      .watch(this.TOPIC_ENDPOINT)
      .pipe(map(message => JSON.parse(message.body)))
      .subscribe((message: MessageDto) => {
        this.receivedMessages.push(message);
      });
  }

  ngOnDestroy(): void {
    this.topicSubscription.unsubscribe();
  }

  sendMessage(): void {
    this.webSocketService.publish({destination: this.MESSAGE_DESTINATION, body: this.message});
  }
}
