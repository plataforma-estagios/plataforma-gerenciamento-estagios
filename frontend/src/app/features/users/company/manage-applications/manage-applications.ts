import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CandidaturaService } from '../../../../shared/services/candidatura.service';
import { EntrevistaService } from '../../../../shared/services/entrevista.service';
import { CandidaturaModel } from '../../../../shared/services/models/CandidaturaModel';
import { EstudanteModel, EstudanteResumoModel } from '../../../../shared/services/models/EstudanteModel';
import { StatusDaCandidatura } from '../../../../shared/services/models/StatusDaCandidatura';
import { FormatoEntrevista } from '../../../../shared/services/models/FormatoEntrevista';
import { Modal } from '../../../../shared/component/modal/modal';
import { LucideAngularModule, EyeIcon, CheckCircleIcon, XCircleIcon, ClockIcon, UserIcon } from 'lucide-angular';
import { DatePipe, CommonModule } from '@angular/common';

@Component({
  selector: 'app-manage-applications',
  standalone: true,
  imports: [CommonModule, RouterModule, Modal, LucideAngularModule, DatePipe, ReactiveFormsModule],
  templateUrl: './manage-applications.html',
  styleUrl: './manage-applications.css',
})
export class ManageApplications implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly candidaturaService = inject(CandidaturaService);
  private readonly entrevistaService = inject(EntrevistaService);
  private readonly fb = inject(FormBuilder);

  vagaId = signal<number>(0);
  candidaturas = signal<CandidaturaModel[]>([]);
  selectedCandidatura = signal<CandidaturaModel | null>(null);

  profileModalOpen = false;
  viewingFullProfile = false;
  studentSummary = signal<EstudanteResumoModel | null>(null);
  studentFullProfile = signal<EstudanteModel | null>(null);

  interviewModalOpen = false;
  interviewForm: FormGroup;
  FormatoEntrevista = FormatoEntrevista;

  EyeIcon = EyeIcon;
  CheckCircleIcon = CheckCircleIcon;
  XCircleIcon = XCircleIcon;
  ClockIcon = ClockIcon;
  UserIcon = UserIcon;

  StatusDaCandidatura = StatusDaCandidatura;

  constructor() {
    this.interviewForm = this.fb.group({
      dataHora: ['', Validators.required],
      formato: [FormatoEntrevista.ONLINE, Validators.required],
      detalhes: ['']
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.vagaId.set(+params['id']);
      this.loadCandidaturas();
    });
  }

  loadCandidaturas() {
    this.candidaturaService.listarCandidaturasDaVaga(this.vagaId()).subscribe({
      next: (data) => this.candidaturas.set(data),
      error: (err) => console.error('Error loading applications', err)
    });
  }

  openProfileModal(candidatura: CandidaturaModel) {
    this.selectedCandidatura.set(candidatura);
    this.viewingFullProfile = false;
    this.candidaturaService.getPerfilResumido(candidatura.id).subscribe({
      next: (data) => {
        this.studentSummary.set(data);
        this.profileModalOpen = true;
      },
      error: (err) => console.error('Error fetching summary', err)
    });
  }

  loadFullProfile() {
    const candidatura = this.selectedCandidatura();
    if (!candidatura) return;

    this.candidaturaService.getPerfilCompleto(candidatura.id).subscribe({
      next: (data) => {
        this.studentFullProfile.set(data);
        this.viewingFullProfile = true;
      },
      error: (err) => console.error('Error fetching full profile', err)
    });
  }

  closeProfileModal() {
    this.profileModalOpen = false;
    this.studentSummary.set(null);
    this.studentFullProfile.set(null);
    this.selectedCandidatura.set(null);
    this.viewingFullProfile = false;
  }

  updateStatus(candidatura: CandidaturaModel, status: StatusDaCandidatura) {
    if (confirm(`Tem certeza que deseja alterar o status para ${status}?`)) {
      this.candidaturaService.atualizarStatus(candidatura.id, status).subscribe({
        next: () => {
          this.loadCandidaturas();
        },
        error: (err) => console.error('Error updating status', err)
      });
    }
  }

  openInterviewModal(candidatura: CandidaturaModel) {
    this.selectedCandidatura.set(candidatura);
    this.interviewModalOpen = true;
    this.interviewForm.reset({
      formato: FormatoEntrevista.ONLINE,
      dataHora: '',
      detalhes: ''
    });
  }

  closeInterviewModal() {
    this.interviewModalOpen = false;
    this.selectedCandidatura.set(null);
  }

  scheduleInterview() {
    if (this.interviewForm.invalid || !this.selectedCandidatura()) return;

    const formValue = this.interviewForm.value;
    const request = {
      candidaturaId: this.selectedCandidatura()!.id,
      dataHora: formValue.dataHora,
      formato: formValue.formato,
      detalhes: formValue.detalhes
    };

    this.entrevistaService.agendarEntrevista(request).subscribe({
      next: () => {
        alert('Entrevista agendada com sucesso!');
        this.closeInterviewModal();
        this.loadCandidaturas();
      },
      error: (err) => {
        console.error('Error scheduling interview', err);
        const errorMessage = typeof err.error === 'string' ? err.error : (err.error?.message || 'Erro desconhecido');
        alert('Erro ao agendar entrevista: ' + errorMessage);
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case StatusDaCandidatura.APROVADA: return 'status-approved';
      case StatusDaCandidatura.REPROVADA: return 'status-rejected';
      case StatusDaCandidatura.ENTREVISTA: return 'status-interview';
      case StatusDaCandidatura.EM_ANÁLISE: return 'status-analysis';
      default: return 'status-default';
    }
  }
}
