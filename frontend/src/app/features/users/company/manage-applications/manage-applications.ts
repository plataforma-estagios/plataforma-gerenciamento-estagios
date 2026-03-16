import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CandidaturaService } from '../../../../shared/services/candidatura.service';
import { EntrevistaService } from '../../../../shared/services/entrevista.service';
import { CandidaturaModel } from '../../../../shared/services/models/CandidaturaModel';
import { EstudanteModel, EstudanteResumoModel } from '../../../../shared/services/models/EstudanteModel';
import { EntrevistaModel } from '../../../../shared/services/models/EntrevistaModel';
import { StatusDaCandidatura } from '../../../../shared/services/models/StatusDaCandidatura';
import { FormatoEntrevista } from '../../../../shared/services/models/FormatoEntrevista';
import { Modal } from '../../../../shared/component/modal/modal';
import { LucideAngularModule, EyeIcon, CheckCircleIcon, XCircleIcon, ClockIcon, UserIcon, FileTextIcon, CalendarIcon, PencilIcon } from 'lucide-angular';
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

  resultModalOpen = false;
  resultForm: FormGroup;
  interviewDetails = signal<EntrevistaModel | null>(null);
  loadingInterview = false;
  submittingResult = false;

  statusModalOpen = false;
  statusForm: FormGroup;
  submittingStatus = false;

  EyeIcon = EyeIcon;
  CheckCircleIcon = CheckCircleIcon;
  XCircleIcon = XCircleIcon;
  ClockIcon = ClockIcon;
  UserIcon = UserIcon;
  FileTextIcon = FileTextIcon;
  CalendarIcon = CalendarIcon;
  PencilIcon = PencilIcon;

  StatusDaCandidatura = StatusDaCandidatura;

  constructor() {
    this.interviewForm = this.fb.group({
      dataHora: ['', Validators.required],
      formato: [FormatoEntrevista.ONLINE, Validators.required],
      detalhes: ['']
    });

    this.resultForm = this.fb.group({
      resultado: ['', Validators.required],
      comentario: ['']
    });

    this.statusForm = this.fb.group({
      status: ['', Validators.required]
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

  openStatusModal(candidatura: CandidaturaModel) {
    this.selectedCandidatura.set(candidatura);
    this.submittingStatus = false;
    this.statusForm.reset({ status: candidatura.status });
    this.statusModalOpen = true;
  }

  closeStatusModal() {
    this.statusModalOpen = false;
    this.selectedCandidatura.set(null);
    this.submittingStatus = false;
  }

  submitStatusChange() {
    if (this.statusForm.invalid || !this.selectedCandidatura()) return;

    const newStatus = this.statusForm.value.status;
    if (newStatus === this.selectedCandidatura()!.status) {
      this.closeStatusModal();
      return;
    }

    this.submittingStatus = true;
    this.candidaturaService.atualizarStatus(this.selectedCandidatura()!.id, newStatus).subscribe({
      next: () => {
        alert('Status atualizado com sucesso!');
        this.closeStatusModal();
        this.loadCandidaturas();
      },
      error: (err) => {
        this.submittingStatus = false;
        console.error('Error updating status', err);
        const errorMessage = typeof err.error === 'string'
          ? err.error
          : (err.error?.message || 'Erro ao atualizar status da candidatura.');
        alert(errorMessage);
      }
    });
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case StatusDaCandidatura.SUBMETIDA: return 'Submetida';
      case StatusDaCandidatura.EM_ANÁLISE: return 'Em Análise';
      case StatusDaCandidatura.ENTREVISTA: return 'Entrevista';
      case StatusDaCandidatura.PROXIMA_ETAPA: return 'Próxima Etapa';
      case StatusDaCandidatura.APROVADA: return 'Aprovada';
      case StatusDaCandidatura.REPROVADA: return 'Reprovada';
      case StatusDaCandidatura.CANCELADA: return 'Cancelada';
      default: return status;
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

  openResultModal(candidatura: CandidaturaModel) {
    this.selectedCandidatura.set(candidatura);
    this.interviewDetails.set(null);
    this.loadingInterview = true;
    this.submittingResult = false;

    this.resultForm.reset({
      resultado: '',
      comentario: ''
    });

    this.entrevistaService.getEntrevistaPorCandidatura(candidatura.id).subscribe({
      next: (data) => {
        this.interviewDetails.set(data);
        this.loadingInterview = false;
        this.resultModalOpen = true;
      },
      error: () => {
        this.loadingInterview = false;
        this.resultModalOpen = true;
      }
    });
  }

  closeResultModal() {
    this.resultModalOpen = false;
    this.selectedCandidatura.set(null);
    this.interviewDetails.set(null);
    this.submittingResult = false;
  }

  submitInterviewResult() {
    if (this.resultForm.invalid || !this.selectedCandidatura()) return;

    this.submittingResult = true;
    const formValue = this.resultForm.value;

    this.candidaturaService.registrarResultadoEntrevista(this.selectedCandidatura()!.id, {
      resultado: formValue.resultado,
      comentario: formValue.comentario || undefined
    }).subscribe({
      next: (response) => {
        alert(response.mensagem || 'Resultado registrado com sucesso!');
        this.closeResultModal();
        this.loadCandidaturas();
      },
      error: (err) => {
        this.submittingResult = false;
        console.error('Error submitting interview result', err);
        const errorMessage = typeof err.error === 'string'
          ? err.error
          : (err.error?.message || 'Erro ao registrar resultado da entrevista.');
        alert(errorMessage);
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case StatusDaCandidatura.APROVADA: return 'status-approved';
      case StatusDaCandidatura.REPROVADA: return 'status-rejected';
      case StatusDaCandidatura.ENTREVISTA: return 'status-interview';
      case StatusDaCandidatura.EM_ANÁLISE: return 'status-analysis';
      case StatusDaCandidatura.PROXIMA_ETAPA: return 'status-next-step';
      default: return 'status-default';
    }
  }

  getFormatoLabel(formato: string): string {
    return formato === FormatoEntrevista.ONLINE ? 'Online' : 'Presencial';
  }
}
