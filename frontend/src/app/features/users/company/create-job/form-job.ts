import { Component, EventEmitter, inject, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { JobsService } from '../../../../shared/services/jobs.service';
import { ToastrService } from 'ngx-toastr';
import { VagaModel } from '../../../../shared/services/models/VagaModel';

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

  @Output() Onclose = new EventEmitter<boolean>();

  constructor(
    private readonly fb: FormBuilder,
  ) {
    this.jobForm = this.fb.group({
      titulo: [this.vaga?.titulo ?? '', [Validators.required]],
      descricao: [this.vaga?.descricao ?? '', [Validators.required]],
      requisitos: [this.vaga?.requisitos ?? '', [Validators.required]],
      areaConhecimento: [this.vaga?.areaConhecimento ?? '', [Validators.required]],
      localizacao: [this.vaga?.localizacao ?? 'PRESENCIAL', [Validators.required]],
      periodoTurno: [this.vaga?.periodoTurno ?? 'MANHA', [Validators.required]],
      prazoCandidatura: [this.vaga?.prazoCandidatura ?? '', [Validators.required]],
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
          this.Onclose.emit(true);
        },
        error: (err) => {
          console.error('Erro ao atualizar:', err);
          this.toastr.error('Erro ao atualizar. Verifique se você está logado como EMPRESA.');
          this.Onclose.emit(false);
        }
      });
    } else {
      this.jobsService.criar(this.jobForm.value).subscribe({
        next: () => {
          this.toastr.success('Vaga publicada com sucesso!');
          this.Onclose.emit(true);
        },
        error: (err) => {
          console.error('Erro ao cadastrar:', err);
          this.toastr.error('Erro ao cadastrar. Verifique se você está logado como EMPRESA.');
          this.Onclose.emit(false);
        }
      });
    }
  }
}