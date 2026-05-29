import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Organization, OrganizationRequest } from '../models/organization.models';

@Injectable({
  providedIn: 'root',
})
export class OrganizationService {
  private readonly apiUrl = 'http://localhost:8080/api/organizations';
  private readonly http = inject(HttpClient);

  findAll(): Observable<Organization[]> {
    return this.http.get<Organization[]>(this.apiUrl);
  }

  findById(id: number): Observable<Organization> {
    return this.http.get<Organization>(`${this.apiUrl}/${id}`);
  }

  create(data: OrganizationRequest): Observable<Organization> {
    return this.http.post<Organization>(this.apiUrl, data);
  }

  update(id: number, data: OrganizationRequest): Observable<Organization> {
    return this.http.put<Organization>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
