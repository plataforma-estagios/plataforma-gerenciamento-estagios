import { Component, signal, OnInit } from '@angular/core';
import { FolderOpen, LucideAngularModule, Pencil, Plus, Trash } from 'lucide-angular';
import { VagaModel } from '../../../shared/services/models/VagaModel';
import { JobsService } from '../../../shared/services/jobs.service';
import { Modal } from '../../../shared/component/modal/modal';
import { FormJob } from './create-job/form-job';
import { DeleteJob } from './delete-job/delete-job';

@Component({
  selector: 'app-company',
  imports: [LucideAngularModule, Modal, FormJob, DeleteJob],
  templateUrl: './company.html',
  styleUrl: './company.css',
})
export class Company implements OnInit {
  readonly PlusIcon = Plus;
  readonly PencilIcon = Pencil;
  readonly TrashIcon = Trash;
  readonly FolderOpenIcon = FolderOpen;

  modalOpen = false;
  modalAction: 'create' | 'edit' | 'delete' = 'create';
  vaga: VagaModel | null = null;

  vagas = signal<VagaModel[]>([]);

  constructor(private readonly jobsService: JobsService) { }

  ngOnInit(): void {
    this.loadVagas();
  }

  loadVagas() {
    this.jobsService.listarMinhasVagas().subscribe((vagas) => {
      this.vagas.set(vagas);
    });
  }

  closeModal() {
    this.modalOpen = false;
    this.vaga = null;
    this.modalAction = 'create';
  }

  handleActionResult(shouldReload?: boolean) {
    this.closeModal();

    if (shouldReload === true) {
      this.loadVagas();
    }
  }

  setCreateModal() {
    this.modalAction = 'create';
    this.vaga = null;
    this.modalOpen = true;
  }

  setEditModal(vaga: VagaModel) {
    this.modalAction = 'edit';
    this.vaga = vaga;
    this.modalOpen = true;
  }

  setDeleteModal(vaga: VagaModel) {
    this.modalAction = 'delete';
    this.vaga = vaga;
    this.modalOpen = true;
  }
}
