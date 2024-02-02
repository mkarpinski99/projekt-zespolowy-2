import { Injectable } from '@angular/core';
import { MySocketService } from './my-socket.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SocketioService {

  constructor(
    private _mySocketService: MySocketService
  ) { }

  join(data: any){
    this._mySocketService.emit('join', data);
  }

  subscribeMessages(): Observable<{username: string, message: string, room: string}> {
    return this._mySocketService.fromEvent('message');
  }

  subscribeViews(): Observable<number> {
    return this._mySocketService.fromEvent('live_viewers');
  }

  sendMessage(data: any) {
    this._mySocketService.emit("text", data);
  }
}
