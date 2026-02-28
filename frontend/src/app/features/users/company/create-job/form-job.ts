import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { JobsService } from '../../../../shared/services/jobs.service';
import { VagaModel } from '../../../../shared/services/models/VagaModel';

function futureOrPresentValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const todayString = `${year}-${month}-${day}`;

    if (value < todayString) {
      return { pastDate: true };
    }
    return null;
  };
}

@Component({
  selector: 'app-form-job',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './form-job.html',
  styleUrl: './form-job.css',
})
export class FormJob implements OnChanges {
  private readonly jobsService = inject(JobsService);
  private readonly toastr = inject(ToastrService);

  @Input() vaga: VagaModel | null = null;

  jobForm: FormGroup;

  @Output() closeModal = new EventEmitter<boolean>();

  get minDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  constructor(private readonly fb: FormBuilder) {
    this.jobForm = this.fb.group({
      titulo: [this.vaga?.titulo ?? '', [Validators.required]],
      descricao: [this.vaga?.descricao ?? '', [Validators.required]],
      requisitos: [this.vaga?.requisitos ?? '', [Validators.required]],
      areaConhecimento: [this.vaga?.areaConhecimento ?? '', [Validators.required]],
      localizacao: [this.vaga?.localizacao ?? 'PRESENCIAL', [Validators.required]],
      periodoTurno: [this.vaga?.periodoTurno ?? 'MANHA', [Validators.required]],
      prazoCandidatura: [this.vaga?.prazoCandidatura ?? '', [Validators.required, futureOrPresentValidator()]],
      beneficios: [this.vaga?.beneficios ?? '', [Validators.required]],
      salario: [this.vaga?.salario ?? 0, [Validators.required, Validators.min(0)]],
      tipoVaga: [this.vaga?.tipoVaga ?? 'ESTAGIO', [Validators.required]],
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!changes['vaga']) return;

    if (!this.vaga) {
      this.jobForm.reset({
        titulo: '',
        descricao: '',
        requisitos: '',
        areaConhecimento: '',
        localizacao: 'PRESENCIAL',
        periodoTurno: 'MANHA',
        prazoCandidatura: '',
        beneficios: '',
        salario: 0,
        tipoVaga: 'ESTAGIO',
      });
      return;
    }

    this.jobForm.patchValue({
      titulo: this.vaga.titulo,
      descricao: this.vaga.descricao,
      requisitos: this.vaga.requisitos,
      areaConhecimento: this.vaga.areaConhecimento,
      localizacao: this.vaga.localizacao,
      periodoTurno: this.vaga.periodoTurno,
      prazoCandidatura: this.formatDateForInput(this.vaga.prazoCandidatura),
      beneficios: this.vaga.beneficios,
      salario: this.vaga.salario,
      tipoVaga: this.vaga.tipoVaga,
    });
  }

  private formatDateForInput(date: string | null | undefined): string {
    if (!date) return '';
    return date.split('T')[0];
  }

  cadastrar() {
    if (this.jobForm.invalid) return;

    if (this.vaga) {
      this.jobsService.atualizar(this.vaga.id.toString(), this.jobForm.value).subscribe({
        next: () => {
          this.toastr.success('Vaga atualizada com sucesso!');
          this.closeModal.emit(true);
        },
        error: (err) => {
          console.error('Erro ao atualizar:', err);
          const errorMessage = typeof err.error === 'string' ? err.error : 'Erro ao atualizar. Verifique os dados.';
          this.toastr.error(errorMessage);
        },
      });
    } else {
      this.jobsService.criar(this.jobForm.value).subscribe({
        next: () => {
          this.toastr.success('Vaga publicada com sucesso!');
          this.closeModal.emit(true);
        },
        error: (err) => {
          console.error('Erro ao cadastrar:', err);
          const errorMessage = typeof err.error === 'string' ? err.error : 'Erro ao cadastrar. Verifique os dados.';
          this.toastr.error(errorMessage);
        },
      });
    }
  }
}
