import { MatButtonModule } from '@angular/material/button';
import { PlayerComponent } from './../../../../shared/components/player/player.component';
import { Component, Input, OnInit } from '@angular/core';
import { ChatboxComponent } from '@features/channel/components/chatbox/chatbox.component';
import { User } from 'app/data/schema/user';
import { UserService } from 'app/data/service/user.service';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from 'app/core/auth/auth.service';
import { SocketioService } from 'app/core/ws/socketio.service';

@Component({
  selector: 'app-channel-details',
  standalone: true,
  imports: [ChatboxComponent, PlayerComponent, MatButtonModule, MatIconModule],
  templateUrl: './channel-details.component.html',
  styleUrl: './channel-details.component.scss'
})
export class ChannelDetailsComponent implements OnInit{
  channelDetails!: User;
  channelUsername: string = '';
  followersCount: number = 0;
  viewersCount: number = 0;
  playerOptions = {
    fluid: true,
    aspectRatio: "16:9",
    autoplay: true,
    sources: [{
      src: ``,
      type: 'application/x-mpegURL'
    }],
    fill: true,
    liveui: true,
    notSupportedMessage: "Użytkownik jest offline",
    language: 'English',
    preload: 'metadata',
    poster: '',
    controls: true,
  }

  @Input()
  set username(username: string){
    this.channelUsername = username
  }

  constructor(
    private _userService: UserService,
    private _authService: AuthService,
    private _socketioService: SocketioService
  ) {}

  ngOnInit(): void {
    this._userService.getUserDetails(this.channelUsername).subscribe({
      next: result => {
        this.channelDetails = result;
        this.playerOptions = {
          fluid: true,
          aspectRatio: "16:9",
          autoplay: false,
          sources: [{
            src: `http://localhost:8080/live/${this.channelDetails.stream_secret_key}.m3u8`,
            type: 'application/x-mpegURL'
          }],
          fill: true,
          language: 'English',
          liveui: true,
          notSupportedMessage: "Użytkownik jest offline",
          preload: 'metadata',
          poster: `https://robohash.org/${this.channelDetails.avatar_path ? this.channelDetails.avatar_path : "https://robohash.org/" + this.channelDetails.username}`,
          controls: true,
        }
        this.fetchFollowersCount();
      },
      error: console.error
    });
    this._socketioService.subscribeViews().subscribe({
      next: res => this.viewersCount = res,
      error: console.error
    });
  }

  followUser(): void {
    this._userService.followUser(this.channelDetails.username).subscribe({
      next: result => {
        this.channelDetails.followed = true;
        this.fetchFollowersCount();
      },
      error: console.error
    })
  }

  unfollowUser(): void {
    this._userService.unfollowUser(this.channelDetails.username).subscribe({
      next: result => {
        this.channelDetails.followed = false;
        this.fetchFollowersCount();
      },
      error: console.error
    })
  }

  userAuthenticated(): boolean {
    return this._authService.isAutheticatedUser()
  }

  getLoggedUsername(): string {
    return this._authService.getUsername();
  }

  fetchFollowersCount(): void {
    this._userService.getFollowersCount(this.channelDetails.username).subscribe({
      next: result => this.followersCount = result,
      error: console.error
    })
  }

}
