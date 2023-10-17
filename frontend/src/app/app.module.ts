import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {WebSocketService} from "./web-socket/web-socket.service";
import {webSocketServiceFactory} from "./web-socket/web-socket-service-factory";
import { MessageComponent } from './message/message.component';
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    MessageComponent
  ],
    imports: [
        BrowserModule,
        FormsModule
    ],
  providers: [
    {
      provide: WebSocketService,
      useFactory: webSocketServiceFactory,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
