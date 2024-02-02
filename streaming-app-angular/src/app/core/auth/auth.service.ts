import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ApiService } from 'app/data/service/api.service';
import { AuthInfo } from './auth-info';
import { AuthCredentials } from './auth-credentials';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAutheticated = false;
  private authority = '';
  private username = '';

  constructor(
    private _api: ApiService
  ) {
    this.isAutheticated = !!localStorage.getItem('username');
    const username = localStorage.getItem('username');
    if (username)
      this.username = username;
    const authority = localStorage.getItem('authority');
    if (authority)
      this.authority = authority;
  }

  login(data: AuthCredentials): Observable<AuthInfo> {
    return this._api.post<AuthInfo>('login', data, {withCredentials: true}).pipe(
      result => {
        result.subscribe({
          next: res => {
            this.authority = res.authority;
            this.username = res.username;
            localStorage.setItem('username', res.username);
            localStorage.setItem('authority', res.authority);
            this.isAutheticated = true;
          }
        })
        return result;
      }
    );
  }

  register(data: AuthCredentials): Observable<any> {
    return this._api.post('users/register', data);
  }

  logout(): void {
    this.isAutheticated = false;
    this.username = '';
    this.authority = '';
    localStorage.removeItem('username');
    localStorage.removeItem('authority');
  }

  isAutheticatedUser(): boolean {
    return this.isAutheticated;
  }

  getUsername(): string {
    return this.username;
  }

  getAuthority(): string {
    return this.authority;
  }
}
