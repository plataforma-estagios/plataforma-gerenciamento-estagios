import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { JobsService } from '../jobs.service';

@Component({
  selector: 'app-jobs-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './jobs-list.html',
  styleUrls: ['./jobs-list.css']
})
export class JobsListComponent implements OnInit {
  private jobsService = inject(JobsService);
  
  vagas: any[] = [];
  erro: string = '';

  ngOnInit(): void {
    this.carregarVagas();
  }

  carregarVagas() {
    this.jobsService.listar().subscribe({
      next: (dados) => {
        this.vagas = dados;
      },
      error: (err) => {
        console.error('Erro ao carregar', err);
        this.erro = 'Não foi possível carregar as vagas. Você está logado?';
      }
    });
  }
}