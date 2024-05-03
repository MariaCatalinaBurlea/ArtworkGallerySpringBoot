import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private router: Router) {}

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/']);
      return false;
    }

    return true;
  }

  public isAdmin(): boolean {
    const token = localStorage.getItem('token');

    var tokenPayload: any;

    if (!token) {
      this.router.navigate(['/']);
      return false;
    }

    try {
      tokenPayload = jwt_decode(token);
    } catch (err) {
      localStorage.clear();
      this.router.navigate(['/']);
    }

    return tokenPayload.role == 'admin';
  }
}
