import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CandidaturaModel } from './models/CandidaturaModel';
import { EstudanteModel, EstudanteResumoModel } from './models/EstudanteModel';
import { StatusDaCandidatura } from './models/StatusDaCandidatura';
import { ResultadoEntrevistaRequestModel, ResultadoEntrevistaResponseModel } from './models/ResultadoEntrevistaModel';

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

  listarCandidaturasDaVaga(vagaId: number): Observable<CandidaturaModel[]> {
    return this.http.get<CandidaturaModel[]>(`${this.apiUrl}/vaga/${vagaId}`, this.getHeaders());
  }

  atualizarStatus(id: number, status: StatusDaCandidatura): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, { status }, this.getHeaders());
  }

  getPerfilResumido(candidaturaId: number): Observable<EstudanteResumoModel> {
    return this.http.get<EstudanteResumoModel>(`${this.apiUrl}/${candidaturaId}/perfil/resumo`, this.getHeaders());
  }

  getPerfilCompleto(candidaturaId: number): Observable<EstudanteModel> {
    return this.http.get<EstudanteModel>(`${this.apiUrl}/${candidaturaId}/perfil`, this.getHeaders());
  }

  registrarResultadoEntrevista(candidaturaId: number, request: ResultadoEntrevistaRequestModel): Observable<ResultadoEntrevistaResponseModel> {
    return this.http.patch<ResultadoEntrevistaResponseModel>(
      `${this.apiUrl}/${candidaturaId}/resultado-entrevista`,
      request,
      this.getHeaders()
    );
  }
}
