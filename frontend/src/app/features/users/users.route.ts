import { Routes } from '@angular/router';
import { roleGuard } from '../../core/services/role.guard';
import { Candidate } from './candidate/candidate';
import { Company } from './company/company';

export const usersRoutes: Routes = [
  {
    path: 'candidate',
    component: Candidate,
    canActivate: [() => roleGuard('CANDIDATE')()],
  },
  {
    path: 'company',
    component: Company,
    canActivate: [() => roleGuard('COMPANY')()],
  },
];
