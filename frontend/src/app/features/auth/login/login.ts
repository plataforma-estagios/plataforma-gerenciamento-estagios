import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { LucideAngularModule, UserCheck, Users } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterModule, LucideAngularModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  readonly Users = Users;
  readonly UserCheck = UserCheck;

  form: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly toastr: ToastrService,
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    this.authService.login({ ...this.form.value }).subscribe({
      next: (res) => {
        localStorage.setItem('auth_token', res.token);

        const role = this.authService.getRole()?.toUpperCase();

        if (role === 'COMPANY') {
          this.router.navigate(['/users/company']);
        } else if (role === 'CANDIDATE') {
          this.router.navigate(['/users/candidate']);
        }

        this.toastr.success('Login realizado com sucesso');
      },
      error: (err) => {
        console.error('Erro de login', err);
        this.toastr.error('Falha na autenticação!');
      },
    });
  }
}
