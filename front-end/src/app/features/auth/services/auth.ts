import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/auth.models';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly http = inject(HttpClient);
  
  private readonly _isLoggedIn = signal<boolean>(!!localStorage.getItem('token'));
  readonly isLoggedIn = this._isLoggedIn.asReadonly();

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          this._isLoggedIn.set(true);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this._isLoggedIn.set(false);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
