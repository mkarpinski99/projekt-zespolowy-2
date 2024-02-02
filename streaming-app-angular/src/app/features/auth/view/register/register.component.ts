import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  userFromGroup: FormGroup = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    repeatPassword: new FormControl('', Validators.required)
  })

  constructor (
    private _authService: AuthService,
    private _snackBar: MatSnackBar,
    private router: Router
    ) {}

  onRegisterSubmit() {
    if (this.userFromGroup.value.password == this.userFromGroup.value.repeatPassword) {
      this._authService.register(this.userFromGroup.value).subscribe({
        next: () => {
          this._snackBar.open("Pomyślnie zarejestrowano", "Zamknij", {duration: 3000});
          this.router.navigate(['/auth/login']);
        },
        error: console.error
      });
    } else {
      this._snackBar.open("Hasła nie są takie same", "Zamknij", {duration: 3000})
    }
  }

  userLogin() {
    this._authService.login(this.userFromGroup.value).subscribe({
      next: console.log,
      error: console.error
    });
  }
}
