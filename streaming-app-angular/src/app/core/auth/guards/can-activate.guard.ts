import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../auth.service';

export const canActivateGuard: CanActivateFn = (route, state) => {
  return inject(AuthService).isAutheticatedUser()
};
