import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { JobsService } from '../../../../shared/services/jobs.service';
import { ToastrService } from 'ngx-toastr';
import { VagaModel } from '../../../../shared/services/models/VagaModel';

@Component({
  selector: 'app-delete-job',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './delete-job.html',
  styleUrl: './delete-job.css',
})
export class DeleteJob {
  private readonly jobsService = inject(JobsService);
  private readonly toastr = inject(ToastrService);

  @Input() vaga: VagaModel | null = null;

  @Output() closeModal = new EventEmitter<boolean>();

  deletar() {
    if (!this.vaga) return;

    this.jobsService.deletar(this.vaga.id.toString()).subscribe({
      next: () => {
        this.toastr.success('Vaga deletada com sucesso!');
        this.closeModal.emit(true);
      },
      error: (err) => {
        console.error('Erro ao deletar:', err);
        this.toastr.error('Erro ao deletar. Verifique se você está logado como EMPRESA.');
        this.closeModal.emit(false);
      }
    });
  }
}
