import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

interface LoginDTO {
  email: string;
  password: string;
}

interface RegisterDTO {
  name?: string;
  email: string;
  password: string;
  cpf?: string;
  cnpj?: string;
  birthdate?: string;
  location?: string;
  website?: string;
  sector?: string;
  role: 'candidate' | 'company';
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private TOKEN_KEY = 'auth_token';

  constructor(private http: HttpClient) {}

  login({ email, password }: LoginDTO) {
    this.http
      .post<{ token: string }>('http://localhost:8080/auth/login', { email, password })
      .subscribe({
        next: (res) => {
          localStorage.setItem(this.TOKEN_KEY, res.token);
        },
        error: () => {
          console.log(email, password);
          console.error('Erro ao autenticar');
        },
      });
    return this.http.post<{ token: string }>('http://localhost:8080/auth/login', {
      email,
      password,
    });
  }

  register({
    //name,
    email,
    password,
    //cpf,
    //cnpj,
    //birthdate,
    //location,
    //website,
    //sector,
    role,
  }: RegisterDTO) {
    return this.http.post('http://localhost:8080/auth/register', {
      // name,
      email,
      password,
      // cpf,
      //cnpj,
      //birthdate,
      //location,
      //website,
      //sector,
      role,
    });
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
      const decodedToken = jwtDecode(token) as { role: 'candidate' | 'company' };
      return decodedToken.role;
    }

    return null;
  }
}
