import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { NotificacaoModel } from './models/NotificacaoModel';

@Injectable({
  providedIn: 'root',
})
export class NotificacaoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/notificacoes`;

  private getHeaders() {
    const token = globalThis.window === undefined ? null : localStorage.getItem('auth_token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
      }),
    };
  }

  listarMinhasNotificacoes(): Observable<NotificacaoModel[]> {
    return this.http.get<NotificacaoModel[]>(this.apiUrl, this.getHeaders());
  }

  contarNaoLidas(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/nao-lidas`, this.getHeaders());
  }

  marcarComoLida(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/lida`, {}, this.getHeaders());
  }
}

