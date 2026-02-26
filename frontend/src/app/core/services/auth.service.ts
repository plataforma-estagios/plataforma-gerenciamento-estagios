import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
  sub: string;
  role: string;
}

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
  private readonly TOKEN_KEY = 'auth_token';

  constructor(private readonly http: HttpClient) {}

  login({ email, password }: LoginDTO) {
    this.http
      .post<{ token: string }>('https://backend-estagios.onrender.com/auth/login', { email, password })
      .subscribe({
        next: (res) => {
          localStorage.setItem(this.TOKEN_KEY, res.token);
        },
        error: () => {
          console.log(email, password);
          console.error('Erro ao autenticar');
        },
      });
    return this.http.post<{ token: string }>('https://backend-estagios.onrender.com/auth/login', {
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
    return this.http.post('https://backend-estagios.onrender.com/auth/register', {
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
