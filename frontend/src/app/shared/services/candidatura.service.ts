import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CandidaturaModel } from './models/CandidaturaModel';

@Injectable({
  providedIn: 'root',
})
export class CandidaturaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/candidatura`;

  private getHeaders() {
    const token = globalThis.window === undefined ? null : localStorage.getItem('auth_token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
      }),
    };
  }

  candidatar(vagaId: number): Observable<any> {
    return this.http.post(this.apiUrl, { vagaId }, this.getHeaders());
  }

  listarMinhasCandidaturas(): Observable<CandidaturaModel[]> {
    return this.http.get<CandidaturaModel[]>(`${this.apiUrl}/minhas-candidaturas`, this.getHeaders());
  }
}
