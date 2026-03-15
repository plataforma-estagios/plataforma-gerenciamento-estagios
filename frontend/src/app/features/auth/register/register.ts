import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  activeTab: 'candidate' | 'company' = 'candidate';

  candidateForm: FormGroup;
  companyForm: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly authService: AuthService,
    private readonly toastr: ToastrService,
  ) {
    this.candidateForm = this.fb.group({
      nome: ['', [Validators.required]],
      cpf: ['', [Validators.required]],
      dataNascimento: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]],
    });

    this.companyForm = this.fb.group({
      razaoSocial: ['', [Validators.required]],
      cnpj: ['', [Validators.required]],
      setor: ['', [Validators.required]],
      localizacao: ['', [Validators.required]],
      link: [''],
      descricao: [''],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]],
    });
  }

  private normalizeDigits(value: unknown) {
    return String(value ?? '').replace(/\D/g, '');
  }

  onSubmit() {
    if (this.activeTab === 'candidate') {
      if (this.candidateForm.invalid) return;

      const raw = this.candidateForm.value;
      const cpf = this.normalizeDigits(raw.cpf);

      if (cpf.length !== 11) {
        this.toastr.error('CPF inválido');
        return;
      }

      this.authService
        .registerCandidate({
          nome: raw.nome,
          cpf,
          dataNascimento: raw.dataNascimento,
          email: String(raw.email).trim(),
          senha: raw.senha,
        })
        .subscribe({
        next: (res) => {
          this.router.navigate(['/auth/login']);
          this.toastr.success('Registro realizado com sucesso');
        },
        error: (err) => {
          console.error('Erro de login', err);
          this.toastr.error('Falha na Registro!');
        },
        });
    } else {
      if (this.companyForm.invalid) return;

      const raw = this.companyForm.value;
      const cnpj = this.normalizeDigits(raw.cnpj);

      if (cnpj.length !== 14) {
        this.toastr.error('CNPJ inválido');
        return;
      }

      this.authService
        .registerCompany({
          razaoSocial: raw.razaoSocial,
          setor: raw.setor,
          cnpj,
          link: raw.link ? String(raw.link).trim() : undefined,
          descricao: raw.descricao ? String(raw.descricao).trim() : undefined,
          localizacao: raw.localizacao,
          email: String(raw.email).trim(),
          senha: raw.senha,
        })
        .subscribe({
        next: (res) => {
          this.router.navigate(['/auth/login']);
          this.toastr.success('Registro realizado com sucesso');
        },
        error: (err) => {
          console.error('Erro de login', err);
          this.toastr.error('Falha na Registro!');
        },
        });
    }
  }
}
