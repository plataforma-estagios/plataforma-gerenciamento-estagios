import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';

import { afterEach, beforeEach, describe, expect, it } from 'vitest';

function makeFakeJwt(payload: object) {
  const header = btoa(JSON.stringify({ alg: 'none', typ: 'JWT' }));
  const body = btoa(JSON.stringify(payload));
  return `${header}.${body}.`;
}

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const TOKEN_KEY = 'auth_token';

  beforeEach(() => {
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [AuthService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('register deve fazer POST no endpoint correto com body correto', () => {
    service.register({ email: 'teste@gmail.com', password: '123', role: 'candidate' }).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      email: 'teste@gmail.com',
      password: '123',
      role: 'candidate',
    });

    req.flush({});
  });

  it('logout deve remover o token do localStorage', () => {
    localStorage.setItem(TOKEN_KEY, 'TOKEN');
    service.logout();
    expect(localStorage.getItem(TOKEN_KEY)).toBeNull();
  });

  it('getToken deve retornar token quando existe', () => {
    localStorage.setItem(TOKEN_KEY, 'ABC');
    expect(service.getToken()).toBe('ABC');
  });

  it('isLoggedIn deve retornar true quando existe token', () => {
    localStorage.setItem(TOKEN_KEY, 'ABC');
    expect(service.isLoggedIn()).toBe(true);
  });

  it('isLoggedIn deve retornar false quando nao existe token', () => {
    localStorage.removeItem(TOKEN_KEY);
    expect(service.isLoggedIn()).toBe(false);
  });

  it('getRole deve retornar role do token', () => {
    const token = makeFakeJwt({ sub: 'teste@gmail.com', role: 'company' });
    localStorage.setItem(TOKEN_KEY, token);
    expect(service.getRole()).toBe('company');
  });

  it('getEmail deve retornar sub (email) do token', () => {
    const token = makeFakeJwt({ sub: 'teste@gmail.com', role: 'candidate' });
    localStorage.setItem(TOKEN_KEY, token);
    expect(service.getEmail()).toBe('teste@gmail.com');
  });

  it('getRole e getEmail devem retornar null quando nao tem token', () => {
    localStorage.removeItem(TOKEN_KEY);
    expect(service.getRole()).toBeNull();
    expect(service.getEmail()).toBeNull();
  });

  it('login deve fazer POST e salvar token no localStorage (seu login atual faz 2 requests)', () => {
    service.login({ email: 'teste@gmail.com', password: '123' }).subscribe();

    const reqs = httpMock.match('http://localhost:8080/auth/login');
    expect(reqs.length).toBe(2);

    for (const req of reqs) {
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ email: 'teste@gmail.com', password: '123' });
      req.flush({ token: 'TOKEN_X' });
    }

    expect(localStorage.getItem(TOKEN_KEY)).toBe('TOKEN_X');
  });
});
