import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { AuthService } from 'app/core/auth/auth.service';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [RouterModule, MatButtonModule,  MatIconModule, MatChipsModule, MatMenuModule, MatToolbarModule],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.scss'
})
export class NavComponent {
  constructor (
    private _authService: AuthService
    ) {}

  isAuthenticated(): boolean {
    return this._authService.isAutheticatedUser();
  }

  logout() {
    this._authService.logout();
  }

  getLoggedUsername(): string {
    return this._authService.getUsername();
  }
}
