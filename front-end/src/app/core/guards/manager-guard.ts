import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth';

export const managerGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isManager()) {
    return true;
  }

  // Se não for gerente, redireciona para o dashboard ou outra página segura
  router.navigate(['/dashboard']);
  return false;
};
