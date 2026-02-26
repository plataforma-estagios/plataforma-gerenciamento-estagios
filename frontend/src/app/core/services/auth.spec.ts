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
  
  const FAKE_USER = {
    email: 'teste@gmail.com',
    password: `pass-${Math.random()}`,
    role: 'candidate' as const, 
  };

  const FAKE_TOKEN = 'TOKEN_GERADO_PELA_API';
  
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
   
    service.register(FAKE_USER).subscribe();

    const req = httpMock.expectOne('https://backend-estagios.onrender.com/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(FAKE_USER);

    req.flush({});
  });

  it('logout deve remover o token do localStorage', () => {
    localStorage.setItem(TOKEN_KEY, FAKE_TOKEN);
    service.logout();
    expect(localStorage.getItem(TOKEN_KEY)).toBeNull();
  });

  it('getToken deve retornar token quando existe', () => {
    localStorage.setItem(TOKEN_KEY, FAKE_TOKEN);
    expect(service.getToken()).toBe(FAKE_TOKEN);
  });

  it('isLoggedIn deve retornar true quando existe token', () => {
    localStorage.setItem(TOKEN_KEY, FAKE_TOKEN);
    expect(service.isLoggedIn()).toBe(true);
  });

  it('isLoggedIn deve retornar false quando nao existe token', () => {
    localStorage.removeItem(TOKEN_KEY);
    expect(service.isLoggedIn()).toBe(false);
  });

  it('getRole deve retornar role do token', () => {
    const roleMock = 'company';
    const token = makeFakeJwt({ sub: FAKE_USER.email, role: roleMock });
    
    localStorage.setItem(TOKEN_KEY, token);
    expect(service.getRole()).toBe(roleMock);
  });

  it('getEmail deve retornar sub (email) do token', () => {
    const token = makeFakeJwt({ sub: FAKE_USER.email, role: FAKE_USER.role });
    
    localStorage.setItem(TOKEN_KEY, token);
    expect(service.getEmail()).toBe(FAKE_USER.email);
  });

  it('getRole e getEmail devem retornar null quando nao tem token', () => {
    localStorage.removeItem(TOKEN_KEY);
    expect(service.getRole()).toBeNull();
    expect(service.getEmail()).toBeNull();
  });

  it('login deve fazer POST e salvar token no localStorage', () => {
    const loginData = { email: FAKE_USER.email, password: FAKE_USER.password };
    
    service.login(loginData).subscribe();

    const reqs = httpMock.match('https://backend-estagios.onrender.com/auth/login');
    expect(reqs.length).toBe(2);

    for (const req of reqs) {
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginData);
      req.flush({ token: FAKE_TOKEN });
    }

    expect(localStorage.getItem(TOKEN_KEY)).toBe(FAKE_TOKEN);
  });
});