import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { Channel } from '../schema/channel';
import { AuthService } from 'app/core/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ChannelService {

  constructor(
    private _api: ApiService,
    private _authService: AuthService
  ) { }

  getAllChannels(): Observable<Array<Channel>> {
    return this._api.get<Array<Channel>>('streams')
  }

  getChannelsByCategory(category: string): Observable<Array<Channel>> {
    return this._api.get<Array<Channel>>(`categories/${category}/streams`);
  }

  getFollowedChannels(): Observable<Array<Channel>> {
    return this._api.get<Array<Channel>>(`users/${this._authService.getUsername()}/following`);
  }
}
