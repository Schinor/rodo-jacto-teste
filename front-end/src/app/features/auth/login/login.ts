import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  errorMessage: string | null = null;
  isLoading = false;

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = null;

      const credentials = this.loginForm.getRawValue();

      this.authService.login(credentials).subscribe({
        next: () => {
          this.router.navigate(['/organizations']);
        },
        error: (err) => {
          this.isLoading = false;
          if (err.status === 403 || err.status === 401) {
            this.errorMessage = 'E-mail ou senha inválidos.';
          } else {
            this.errorMessage = 'Ocorreu um erro ao tentar fazer login. Tente novamente mais tarde.';
          }
        }
      });
    }
  }
}
