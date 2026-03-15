import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { tap } from 'rxjs';
import { environment } from '../../../environments/environment';

interface JwtPayload {
  sub: string;
  role: string;
}

interface LoginDTO {
  email: string;
  password: string;
}

interface CandidateRegisterDTO {
  nome: string;
  cpf: string;
  curso?: string;
  instituicao?: string;
  dataNascimento: string;
  email: string;
  senha: string;
}

interface CompanyRegisterDTO {
  razaoSocial: string;
  setor: string;
  cnpj: string;
  link?: string;
  descricao?: string;
  localizacao: string;
  email: string;
  senha: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly TOKEN_KEY = 'auth_token';

  constructor(private readonly http: HttpClient) {}

  login({ email, password }: LoginDTO) {
    return this.http
      .post<{ token: string }>(`${environment.apiUrl}/auth/login`, { email, password })
      .pipe(
        tap({
          next: (res) => {
            localStorage.setItem(this.TOKEN_KEY, res.token);
          },
          error: () => {
            console.error('Erro ao autenticar');
          },
        })
      );
  }

  registerCandidate(dto: CandidateRegisterDTO) {
    return this.http.post(`${environment.apiUrl}/auth/register/candidato`, dto);
  }

  registerCompany(dto: CompanyRegisterDTO) {
    return this.http.post(`${environment.apiUrl}/auth/register/empresa`, dto);
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  getToken() {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn() {
    return this.getToken() !== null;
  }

  getRole() {
    const token = this.getToken();
    if (token) {
      const decodedToken = jwtDecode<JwtPayload>(token);
      return decodedToken.role;
    }

    return null;
  }

  getEmail() {
    const token = this.getToken();
    if (token) {
      const decodedToken = jwtDecode<JwtPayload>(token);
      return decodedToken.sub;
    }

    return null;
  }
}
