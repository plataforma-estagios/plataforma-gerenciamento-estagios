import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { VagaModel } from './models/VagaModel';

@Injectable({
  providedIn: 'root',
})
export class JobsService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/vagas`;

  private getHeaders() {
    const token = globalThis.window === undefined ? null : localStorage.getItem('auth_token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
      }),
    };
  }

  listarMinhasVagas(): Observable<VagaModel[]> {
    return this.http.get<VagaModel[]>(`${this.apiUrl}/minhas-vagas`, this.getHeaders());
  }

  listar(): Observable<VagaModel[]> {
    return this.http.get<VagaModel[]>(this.apiUrl, this.getHeaders());
  }

  buscarPorId(id: string): Observable<VagaModel> {
    return this.http.get<VagaModel>(`${this.apiUrl}/${id}`, this.getHeaders());
  }

  criar(vaga: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, vaga, this.getHeaders());
  }

  atualizar(id: string, vaga: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, vaga, this.getHeaders());
  }

  deletar(id: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`, this.getHeaders());
  }
}
