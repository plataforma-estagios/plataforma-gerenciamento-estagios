import { inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from './auth.service';

export const roleGuard = (expectedRole: string) => {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const role = authService.getRole();

    if (role === expectedRole) {
      return true;
    }

    router.navigate(['/auth/unauthorized']);
    return false;
  };
};
