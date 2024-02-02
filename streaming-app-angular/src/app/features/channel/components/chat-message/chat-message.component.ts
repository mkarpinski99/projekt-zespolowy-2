import { Component, Input, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { AuthService } from 'app/core/auth/auth.service';

@Component({
  selector: 'app-chat-message',
  standalone: true,
  imports: [MatListModule, MatCardModule],
  templateUrl: './chat-message.component.html',
  styleUrl: './chat-message.component.scss'
})
export class ChatMessageComponent implements OnInit{
  @Input()
  message!: {
    username: string,
    message: string
  }

  msgColor: string = "default-message-background";

  constructor (
    private _authService: AuthService
  ) {}

  ngOnInit(): void {
    if(this.message && this._authService.getUsername() == this.message.username){
      this.msgColor = 'my-message-background';
    }
  }
}
