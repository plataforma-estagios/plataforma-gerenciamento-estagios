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
      name: ['', [Validators.required]],
      //cpf: ['', [Validators.required, Validators.minLength(11)]],
      //birthdate: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });

    this.companyForm = this.fb.group({
      name: ['', [Validators.required]],
      //cnpj: ['', [Validators.required, Validators.minLength(14)]],
      //location: ['', [Validators.required]],
      //website: ['', [Validators.required, Validators.pattern(/^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/)]],
      //sector: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.activeTab === 'candidate') {
      if (this.candidateForm.invalid) return;

      this.authService.register({ ...this.candidateForm.value, role: 'CANDIDATE' }).subscribe({
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

      this.authService.register({ ...this.companyForm.value, role: 'COMPANY' }).subscribe({
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
