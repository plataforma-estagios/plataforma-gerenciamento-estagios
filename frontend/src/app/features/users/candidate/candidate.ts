import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';

import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { JobsService } from '../../../shared/services/jobs.service';
import { VagaModel } from '../../../shared/services/models/VagaModel';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './candidate.html',
  styleUrl: './candidate.css',
})
export class Candidate implements OnInit {
  private readonly jobsService = inject(JobsService);
  private readonly authService = inject(AuthService);

  role: string | null = '';
  email: string | null = '';

  vagas: VagaModel[] = [];
  vagasFiltradas: VagaModel[] = [];

  filtroArea: string = '';
  filtroTipo: string = '';
  filtroModelo: string = '';

  ngOnInit(): void {
    this.role = this.authService.getRole();
    this.email = this.authService.getEmail();
    this.carregarVagas();
  }
  private readonly cdr = inject(ChangeDetectorRef);

  carregarVagas() {
    this.jobsService.listar().subscribe({
      next: (dados) => {
        this.vagas = dados.sort(
          (a, b) => new Date(b.dataPublicacao).getTime() - new Date(a.dataPublicacao).getTime(),
        );
        this.aplicarFiltros();
        this.cdr.detectChanges();
        console.log(dados);
      },
      error: (err) => {
        console.error('Erro ao carregar', err);
      },
    });
  }

  aplicarFiltros() {
    if (!this.vagas) return;

    this.vagasFiltradas = this.vagas.filter((vaga) => {
      const matchArea =
        !this.filtroArea ||
        vaga.areaConhecimento.toLowerCase().includes(this.filtroArea.toLowerCase());

      const matchTipo =
        !this.filtroTipo || vaga.tipoVaga.toLowerCase().includes(this.filtroTipo.toLowerCase());

      const matchModelo =
        !this.filtroModelo ||
        vaga.localizacao.toLowerCase().includes(this.filtroModelo.toLowerCase());

      return matchArea && matchTipo && matchModelo && vaga.status === 'EM_ABERTO';
    });
  }
}
