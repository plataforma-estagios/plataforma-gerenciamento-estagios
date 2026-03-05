import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Eye, Building2, MapPin, DollarSign, Clock, Briefcase, Calendar } from 'lucide-angular';
import { CandidaturaService } from '../../../../shared/services/candidatura.service';
import { JobsService } from '../../../../shared/services/jobs.service';
import { CandidaturaModel } from '../../../../shared/services/models/CandidaturaModel';
import { VagaModel } from '../../../../shared/services/models/VagaModel';
import { Modal } from '../../../../shared/component/modal/modal';

@Component({
  selector: 'app-my-applications',
  standalone: true,
  imports: [CommonModule, LucideAngularModule, Modal],
  templateUrl: './my-applications.html',
  styleUrl: './my-applications.css',
})
export class MyApplications implements OnInit {
  private readonly candidaturaService = inject(CandidaturaService);
  private readonly jobsService = inject(JobsService);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly EyeIcon = Eye;
  readonly BuildingIcon = Building2;
  readonly MapPinIcon = MapPin;
  readonly DollarSignIcon = DollarSign;
  readonly ClockIcon = Clock;
  readonly BriefcaseIcon = Briefcase;
  readonly CalendarIcon = Calendar;

  candidaturas: CandidaturaModel[] = [];
  modalOpen = false;
  selectedJob: VagaModel | null = null;

  ngOnInit(): void {
    this.loadCandidaturas();
  }

  loadCandidaturas() {
    this.candidaturaService.listarMinhasCandidaturas().subscribe({
      next: (data) => {
        this.candidaturas = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erro ao carregar candidaturas', err);
      },
    });
  }

  viewDetails(candidatura: CandidaturaModel) {
    this.jobsService.buscarPorId(candidatura.vagaId.toString()).subscribe({
      next: (vaga) => {
        this.selectedJob = vaga;
        this.modalOpen = true;
      },
      error: (err) => {
        console.error('Erro ao carregar detalhes da vaga', err);
      }
    });
  }

  closeModal() {
    this.modalOpen = false;
    this.selectedJob = null;
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('pt-BR');
  }

  getStatusLabel(status: string): string {
    const statusMap: Record<string, string> = {
      'SUBMETIDA': 'Submetida',
      'EM_ANÁLISE': 'Em Análise',
      'ENTREVISTA': 'Entrevista',
      'APROVADA': 'Aprovada',
      'REPROVADA': 'Reprovada',
      'CANCELADA': 'Cancelada'
    };
    return statusMap[status] || status;
  }

  getStatusClass(status: string): string {
    const classMap: Record<string, string> = {
      'SUBMETIDA': 'em-analise',
      'EM_ANÁLISE': 'em-analise',
      'ENTREVISTA': 'entrevista',
      'APROVADA': 'aprovado',
      'REPROVADA': 'recusado',
      'CANCELADA': 'recusado'
    };
    return classMap[status] || '';
  }
}
