import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Category } from '../model/category.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  apiServerUrl = `${environment.apiUrl}/category`;

  constructor(private http: HttpClient) { }

  addCategory(data: any): Observable<Category> {
    return this.http.post<Category>(`${this.apiServerUrl}/add`, data);
  }

  updateCategory(data: any): Observable<Category> {
    return this.http.put<Category>(`${this.apiServerUrl}/update`, data, {
      headers: new HttpHeaders().set('Content-Type' , "application/json")
    });
  }

  deleteCategory(id: any): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/delete/${id}`);
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiServerUrl}/get`);
  }

  getCategoryById(id:number): Observable<Category> {
    return this.http.get<Category>(`${this.apiServerUrl}/getById/${id}`);
  }
}
