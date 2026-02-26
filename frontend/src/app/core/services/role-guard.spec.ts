import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { AuthService } from './auth.service';
import { roleGuard } from './role.guard';

describe('roleGuard', () => {
  let mockAuthService: any;
  let router: Router;

  beforeEach(() => {
    mockAuthService = {
      getRole: vi.fn(),
    };

    TestBed.resetTestingModule();

    TestBed.configureTestingModule({
      providers: [provideRouter([]), { provide: AuthService, useValue: mockAuthService }],
    });

    router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
  });

  it('deve retornar true quando a role for igual à esperada', () => {
    mockAuthService.getRole.mockReturnValue('company');

    const guardFn = roleGuard('company');

    const result = TestBed.runInInjectionContext(() => guardFn());

    expect(result).toBe(true);
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('deve redirecionar e retornar false quando a role for diferente', () => {
    mockAuthService.getRole.mockReturnValue('candidate');

    const guardFn = roleGuard('company');

    const result = TestBed.runInInjectionContext(() => guardFn());

    expect(result).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['/auth/unauthorized']);
  });
});
