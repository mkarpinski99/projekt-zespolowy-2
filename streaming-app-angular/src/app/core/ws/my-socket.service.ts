import { Injectable } from '@angular/core';
import { Socket } from 'ngx-socket-io';

@Injectable({
  providedIn: 'root'
})
export class MySocketService extends Socket {

  constructor() {
    super(
      {
        url: 'http://localhost:6969',
        options: {
          transports: ['websocket']
        }
      })
  }
}
