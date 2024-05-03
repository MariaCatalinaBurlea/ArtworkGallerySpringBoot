import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Artwork } from '../model/artwork.model';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class ArtworkService {
  apiServerUrl = `${environment.apiUrl}/artwork`;

  constructor(private http: HttpClient) {}

  addArtwork(artwork: Artwork): Observable<Artwork> {
    return this.http.post<Artwork>(`${this.apiServerUrl}/add`, artwork);
  }

  updateArtwork(artwork: Artwork): Observable<Artwork> {
    return this.http.put<Artwork>(`${this.apiServerUrl}/update`, artwork, {
      headers: new HttpHeaders().set('Content-Type', 'application/json'),
    });
  }

  deleteArtwork(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/delete/${id}`);
  }

  getAllArtworks(): Observable<Artwork[]> {
    return this.http.get<Artwork[]>(`${this.apiServerUrl}/get`);
  }

  getArtworksByCategory(id:number): Observable<Artwork[]> {
    return this.http.get<Artwork[]>(`${this.apiServerUrl}/getByCategory/${id}`);
  }
}
