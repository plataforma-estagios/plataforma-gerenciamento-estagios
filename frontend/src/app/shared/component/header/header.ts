import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Bell, Check, ChevronDown, LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { NotificacaoService } from '../../services/notificacao.service';
import { NotificacaoModel } from '../../services/models/NotificacaoModel';

@Component({
  selector: 'app-header',
  imports: [CommonModule, LucideAngularModule, RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit, OnDestroy {
  readonly BellIcon = Bell;
  readonly CheckIcon = Check;
  readonly ChevronDown = ChevronDown;

  isProfileMenuOpen = false;
  isNotificationsMenuOpen = false;

  unreadCount = 0;
  notifications: NotificacaoModel[] = [];
  isLoadingNotifications = false;

  role: string | null = null;

  private pollIntervalId: ReturnType<typeof setInterval> | null = null;

  constructor(
    private readonly authService: AuthService,
    private readonly notificacaoService: NotificacaoService,
    private readonly router: Router
  ) {
    const rawRole = this.authService.getRole();
    this.role = rawRole ? rawRole.toUpperCase().replace(/^ROLE_/, '') : null;
  }

  ngOnInit(): void {
    if (this.role === 'CANDIDATE') {
      this.refreshUnreadCount();
      this.pollIntervalId = setInterval(() => this.refreshUnreadCount(), 30000);
    }
  }

  ngOnDestroy(): void {
    if (this.pollIntervalId) {
      clearInterval(this.pollIntervalId);
      this.pollIntervalId = null;
    }
  }

  toggleProfileMenu() {
    this.isNotificationsMenuOpen = false;
    this.isProfileMenuOpen = !this.isProfileMenuOpen;
  }

  toggleNotificationsMenu() {
    this.isProfileMenuOpen = false;
    this.isNotificationsMenuOpen = !this.isNotificationsMenuOpen;

    if (this.isNotificationsMenuOpen) {
      this.loadNotifications();
    }
  }

  refreshUnreadCount() {
    if (this.role !== 'CANDIDATE') return;

    this.notificacaoService.contarNaoLidas().subscribe({
      next: (count) => {
        this.unreadCount = count;
      },
      error: () => {
        this.unreadCount = 0;
      },
    });
  }

  loadNotifications() {
    if (this.role !== 'CANDIDATE') return;

    this.isLoadingNotifications = true;

    this.notificacaoService.listarMinhasNotificacoes().subscribe({
      next: (data) => {
        this.notifications = [...data].sort((a, b) => (a.dataEnvio < b.dataEnvio ? 1 : -1));
        this.unreadCount = this.notifications.filter((n) => !n.lida).length;
        this.isLoadingNotifications = false;
      },
      error: () => {
        this.notifications = [];
        this.isLoadingNotifications = false;
      },
    });
  }

  markAsRead(notification: NotificacaoModel) {
    if (notification.lida) return;

    this.notificacaoService.marcarComoLida(notification.id).subscribe({
      next: () => {
        notification.lida = true;
        this.unreadCount = Math.max(0, this.unreadCount - 1);
      },
      error: () => {},
    });
  }

  handleLogout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}
