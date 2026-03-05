import { Routes } from '@angular/router';
import { roleGuard } from '../../core/services/role.guard';
import { Candidate } from './candidate/candidate';
import { Company } from './company/company';
import { MyApplications } from './candidate/my-applications/my-applications';

export const usersRoutes: Routes = [
  {
    path: 'candidate/my-applications',
    component: MyApplications,
    canActivate: [() => roleGuard('CANDIDATE')()],
  },
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
