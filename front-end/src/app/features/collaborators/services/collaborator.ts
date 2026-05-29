import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Collaborator, CollaboratorRequest } from '../models/collaborator.models';

@Injectable({
  providedIn: 'root',
})
export class CollaboratorService {
  private readonly apiUrl = 'http://localhost:8080/api/collaborators';
  private readonly http = inject(HttpClient);

  findAll(): Observable<Collaborator[]> {
    return this.http.get<Collaborator[]>(this.apiUrl);
  }

  findById(id: number): Observable<Collaborator> {
    return this.http.get<Collaborator>(`${this.apiUrl}/${id}`);
  }

  create(data: CollaboratorRequest): Observable<Collaborator> {
    return this.http.post<Collaborator>(this.apiUrl, data);
  }

  update(id: number, data: CollaboratorRequest): Observable<Collaborator> {
    return this.http.put<Collaborator>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
