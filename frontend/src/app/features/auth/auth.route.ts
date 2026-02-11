import { Routes } from '@angular/router';
import { loginGuard } from '../../core/services/login.guard';
import { Login } from './login/login';
import { Register } from './register/register';
import { Unauthorized } from './unauthorized/unauthorized';

export const authRoutes: Routes = [
  {
    path: 'login',
    component: Login,
    canActivate: [loginGuard],
  },
  {
    path: 'register',
    component: Register,
  },
  {
    path: 'unauthorized',
    component: Unauthorized,
  },
];
