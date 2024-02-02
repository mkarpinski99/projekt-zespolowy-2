import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormField, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from 'app/core/auth/auth.service';
import { User } from 'app/data/schema/user';
import { UserService } from 'app/data/service/user.service';

@Component({
  selector: 'app-user-settings',
  standalone: true,
  imports: [MatFormFieldModule, ReactiveFormsModule, MatInputModule, MatButtonModule, FormsModule, MatFormFieldModule],
  templateUrl: './user-settings.component.html',
  styleUrl: './user-settings.component.scss'
})
export class UserSettingsComponent implements OnInit {
  streamSecretKey: string = '';
  user!: User;
  newTitle: string = ''

  constructor (
    private _userService: UserService,
    private _authService: AuthService
  ) {}

  ngOnInit(): void {
    this.fetchUserData();
  }

  fetchUserData() {
    this._userService.getUserDetails(this._authService.getUsername()).subscribe({
      next: result => this.user = result,
      error: console.error
    })
  }

  changeTitle(){
    this._userService.changeTitle(this._authService.getUsername(), {stream_title: this.newTitle}).subscribe({
      next: result => this.user = result,
      error: console.log
    })
  }
}
