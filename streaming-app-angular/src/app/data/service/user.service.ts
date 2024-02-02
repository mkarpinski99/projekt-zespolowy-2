import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { User } from '../schema/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private _api: ApiService
  ) { }

  getUserDetails(username: string): Observable<User> {
    return this._api.get<User>(`users/${username}`)
  }

  followUser(username: string): Observable<any> {
    return this._api.post(`users/${username}/follow`, {});
  }

  unfollowUser(username: string): Observable<any> {
    return this._api.post(`users/${username}/unfollow`, {});
  }

  getFollowersCount(username: string): Observable<number> {
    return this._api.get(`users/${username}/followers/count`)
  }

  changeTitle(username: string, body: {stream_title: string}): Observable<any> {
    return this._api.put(`users/${username}`, body)
  }
}
