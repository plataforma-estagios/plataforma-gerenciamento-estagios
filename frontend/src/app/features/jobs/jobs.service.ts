import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'; // Importamos HttpParams
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class JobsService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/vagas';

  private getHeaders() {
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    };
  }

  listar(area?: string, tipo?: string, localizacao?: string, sortBy?: string): Observable<any[]> {
    let params = new HttpParams();

    if (area) params = params.set('area', area);
    if (tipo) params = params.set('tipo', tipo);
    if (localizacao) params = params.set('localizacao', localizacao);
    if (sortBy) params = params.set('sortBy', sortBy);

    return this.http.get<any[]>(this.apiUrl, { 
      ...this.getHeaders(), 
      params 
    });
  }

  criar(vaga: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, vaga, this.getHeaders());
  }
}