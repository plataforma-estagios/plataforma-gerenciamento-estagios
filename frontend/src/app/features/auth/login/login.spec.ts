import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../core/services/auth.service';
import { Login } from './login';

describe('Login Component', () => {
  let fixture: ComponentFixture<Login>;
  let component: Login;

  let authMock: { login: any; getRole: any };
  let router: Router;
  let toastrMock: { success: any; error: any };

  beforeEach(async () => {
    authMock = {
      login: vi.fn(),
      getRole: vi.fn(),
    };

    toastrMock = {
      success: vi.fn(),
      error: vi.fn(),
    };

    TestBed.resetTestingModule();

    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        provideRouter([]),

        { provide: AuthService, useValue: authMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');

    localStorage.clear();
  });

  it('não deve chamar login se o form for inválido', () => {
    component.form.setValue({ email: '', password: '' });
    component.onSubmit();

    expect(authMock.login).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
    expect(toastrMock.success).not.toHaveBeenCalled();
  });

  it('deve logar, salvar token e navegar para COMPANY', () => {
    component.form.setValue({ email: 'teste@gmail.com', password: '123' });

    authMock.login.mockReturnValue(of({ token: 'TOKEN_X' }));
    authMock.getRole.mockReturnValue('company');

    component.onSubmit();

    expect(authMock.login).toHaveBeenCalledWith({ email: 'teste@gmail.com', password: '123' });
    expect(localStorage.getItem('auth_token')).toBe('TOKEN_X');
    expect(router.navigate).toHaveBeenCalledWith(['/users/company']);
    expect(toastrMock.success).toHaveBeenCalledWith('Login realizado com sucesso');
  });

  it('deve logar, salvar token e navegar para CANDIDATE', () => {
    component.form.setValue({ email: 'teste@gmail.com', password: '123' });

    authMock.login.mockReturnValue(of({ token: 'TOKEN_Y' }));
    authMock.getRole.mockReturnValue('candidate');

    component.onSubmit();

    expect(localStorage.getItem('auth_token')).toBe('TOKEN_Y');
    expect(router.navigate).toHaveBeenCalledWith(['/users/candidate']);
    expect(toastrMock.success).toHaveBeenCalledWith('Login realizado com sucesso');
  });

  it('deve mostrar toastr de erro quando falhar o login', () => {
    component.form.setValue({ email: 'teste@gmail.com', password: '123' });

    authMock.login.mockReturnValue(throwError(() => new Error('401')));

    component.onSubmit();

    expect(toastrMock.error).toHaveBeenCalledWith('Falha na autenticação!');
  });
});
