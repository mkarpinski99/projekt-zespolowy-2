import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { ChatMessageComponent } from '../chat-message/chat-message.component';
import { AuthService } from 'app/core/auth/auth.service';
import { SocketioService } from 'app/core/ws/socketio.service';

@Component({
  selector: 'app-chatbox',
  standalone: true,
  imports: [
    MatCardModule,
    MatListModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    ChatMessageComponent
  ],
  templateUrl: './chatbox.component.html',
  styleUrl: './chatbox.component.scss'
})
export class ChatboxComponent implements OnInit {
  messages: Array<any> = [];
  newMessage: string = '';
  @Input()
  streamSecretKey!: string;
  @Input()
  streamerUsername!: string;
  @ViewChild('listEnd') private listEnd!: ElementRef;

  constructor (
    private _authService: AuthService,
    private _socketioService: SocketioService
  ) {}

  ngOnInit(): void {
    this._socketioService.join({
      room: this.streamSecretKey,
      streamerUsername: this.streamerUsername,
      joinerUsername: this._authService.getUsername(),
    })
    this._socketioService.subscribeMessages().subscribe({
      next: result => {
        if (result.room == this.streamSecretKey) {
          this.messages.push(result)
        }
      },
      error: console.error
    })
  }

  sendNewMessage() {
    if (this.newMessage.length != 0 && this.isAuthenticated()){
      this._socketioService.sendMessage(
        {
          message: this.newMessage,
          username: this._authService.getUsername(),
          room: this.streamSecretKey
        }
      )
      this.newMessage = '';
      this.listEnd.nativeElement.scrollIntoView({ behavior: "smooth"});
    }
  }

  isAuthenticated(): boolean {
    return this._authService.isAutheticatedUser();
  }
}
