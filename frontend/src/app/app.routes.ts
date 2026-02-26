import { Routes } from '@angular/router';
import { authRoutes } from './features/auth/auth.route';
import { usersRoutes } from './features/users/users.route';
import { MainLayout } from './shared/layout/main-layout/main-layout';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full',
  },
  {
    path: 'auth',
    children: authRoutes,
  },
  {
    path: 'users',
    component: MainLayout,
    children: usersRoutes,
  },
  {
    path: '**',
    redirectTo: 'auth/login',
  },
];
