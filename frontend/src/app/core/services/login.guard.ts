import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const loginGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    const role = authService.getRole()?.toUpperCase();

    if (role === 'COMPANY' || role === 'ROLE_COMPANY') {
      return router.createUrlTree(['/users/company']);
    }

    return router.createUrlTree(['/users/candidate']);
  }

  return true;
};
