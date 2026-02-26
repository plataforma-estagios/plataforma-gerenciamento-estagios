import { Component } from '@angular/core';
import { ChevronDown, LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [LucideAngularModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  readonly ChevronDown = ChevronDown;

  isProfileMenuOpen = false;

  role: string | null = null;

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    this.role = this.authService.getRole();
  }

  toggleProfileMenu() {
    this.isProfileMenuOpen = !this.isProfileMenuOpen;
  }

  handleLogout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}
