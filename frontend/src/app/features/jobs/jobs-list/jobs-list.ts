import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { JobsService } from '../jobs.service';

@Component({
  selector: 'app-jobs-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './jobs-list.html',
  styleUrls: ['./jobs-list.css']
})
export class JobsListComponent implements OnInit {
  private jobsService = inject(JobsService);
  
  vagas: any[] = [];
  erro: string = '';

  filtroArea: string = '';
  filtroTipo: string = '';
  filtroLocal: string = '';
  ordenacao: string = 'dataPublicacao';

  ngOnInit(): void {
    this.carregarVagas();
  }

  carregarVagas() {
    this.jobsService.listar(
      this.filtroArea, 
      this.filtroTipo, 
      this.filtroLocal, 
      this.ordenacao
    ).subscribe({
      next: (dados) => {
        this.vagas = dados;
        this.erro = '';
      },
      error: (err) => {
        console.error('Erro ao carregar', err);
        this.erro = 'Não foi possível carregar as vagas. Verifique sua conexão ou login.';
      }
    });
  }

  aplicarFiltros() {
    this.carregarVagas();
  }
}