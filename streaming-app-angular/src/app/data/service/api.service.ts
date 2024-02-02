import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private API_URL: string = "http://localhost:5000"
  private API_SUFFIX: string = "/api"
  private headers: any = {
    'Content-Type': 'application/json'
  }

  constructor(private http: HttpClient) { }

  get<T>(url: string, params?: any, suffix: string = this.API_SUFFIX): Observable<T> {
    const options = {
      headers: this.headers,
      params: params ? params : {}
    }
    return this.http.get<T>(this.API_URL+suffix+'/'+url, options)
  }

  post<T>(url: string, body: any, params?: any, suffix: string = this.API_SUFFIX): Observable<T> {
    const options = {
      headers: this.headers,
      params: params ? params : {}
    }
    return this.http.post<T>(this.API_URL+suffix+'/'+url, body, options)
  }

  put<T>(url: string, body: any, params?: any, suffix: string = this.API_SUFFIX): Observable<T> {
    const options = {
      headers: this.headers,
      params: params ? params : {}
    }
    return this.http.put<T>(this.API_URL+suffix+'/'+url, body, options)
  }
}
