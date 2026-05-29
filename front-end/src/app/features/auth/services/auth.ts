import { Injectable, inject, signal, computed } from '@angular/core';
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

  // Signal para armazenar o nível de acesso do usuário atual
  private readonly _accessLevel = signal<string | null>(this.getAccessLevelFromToken());
  readonly accessLevel = this._accessLevel.asReadonly();

  // Helpers computados para facilitar o uso no restante da aplicação
  readonly isManager = computed(() => this._accessLevel() === 'MANAGER');
  readonly isOperator = computed(() => this._accessLevel() === 'OPERATOR');

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          this._isLoggedIn.set(true);
          this._accessLevel.set(this.getAccessLevelFromToken());
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this._isLoggedIn.set(false);
    this._accessLevel.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private getAccessLevelFromToken(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      // O JWT é composto por 3 partes separadas por ponto. A segunda parte é o payload Base64
      const payloadBase64 = token.split('.')[1];
      const payloadJson = atob(payloadBase64);
      const payload = JSON.parse(payloadJson);
      
      // O claim 'role' foi definido no TokenService.kt do backend
      return payload.role || null;
    } catch (e) {
      console.error('Erro ao decodificar token', e);
      return null;
    }
  }
}
