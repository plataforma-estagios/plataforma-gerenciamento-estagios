import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { JobsService } from '../jobs.service';

@Component({
  selector: 'app-create-job',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './create-job.html',
  styleUrl: './create-job.css',
})
export class CreateJob {
  private jobsService = inject(JobsService);
  private router = inject(Router);

  // Objeto seguindo exatamente o VagaRequestDTO do seu colega
  vaga = {
    titulo: '',
    descricao: '',
    requisitos: '',
    areaConhecimento: '',
    localizacao: 'PRESENCIAL', 
    periodoTurno: '',
    prazoCandidatura: '',
    beneficios: '',
    salario: '',
    tipoVaga: 'ESTAGIO' 
  };

  cadastrar() {
    this.jobsService.criar(this.vaga).subscribe({
      next: () => {
        alert('Vaga publicada com sucesso! 🚀');
        this.router.navigate(['/jobs']);
      },
      error: (err) => {
        console.error('Erro ao cadastrar:', err);
        alert('Erro ao cadastrar. Verifique se você está logado como EMPRESA.');
      }
    });
  }
}