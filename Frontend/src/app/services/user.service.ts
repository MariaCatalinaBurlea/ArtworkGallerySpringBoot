import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  apiServerUrl = `${environment.apiUrl}/user`
  constructor(private http: HttpClient) {}

  signup(user: User) {
    return this.http.post(`${this.apiServerUrl}/signup`, user, {
      headers: new HttpHeaders().set('Content-type', 'application/json'),
    });
  }

  login(credentials: { email: string; password: string }) {
    return this.http.post(`${this.apiServerUrl}/login`, credentials, {
      headers: new HttpHeaders().set('Content-type', 'application/json'),
    });
  }

  checkToken() {
    return this.http.get(`${this.apiServerUrl}/checkToken`);
  }

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiServerUrl}/getAll`);
  }

  updateStatus(data: any) {
    return this.http.put(`${this.apiServerUrl}/updateStatus`, data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json'),
    });
  }

  updateUser(data: any): Observable<User> {
    return this.http.put<User>(`${this.apiServerUrl}/update`, data, {
      headers: new HttpHeaders().set('Content-Type' , "application/json")
    });
  }

  deleteUser(id: number){
    return this.http.delete<void>(`${this.apiServerUrl}/delete/${id}`);
  }

  getByRole(role: string){
    return this.http.get<User[]>(`${this.apiServerUrl}/getByRole/${role}`);
  }

  getUsers(){
    return this.http.get<User[]>(`${this.apiServerUrl}/getUsers`);
  }

  getAdminsEmails(){
    return this.http.get<String[]>(`${this.apiServerUrl}/getAdminEmails`);
  }

  changePassword(credentials: { email: string; password: string }) {
    return this.http.put(`${this.apiServerUrl}/changePasword`, credentials, {
      headers:new HttpHeaders().set('Content-Type', 'application/json')
    })
  }
}
