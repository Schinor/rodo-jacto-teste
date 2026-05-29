import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Device, DeviceRequest } from '../models/device.models';

@Injectable({
  providedIn: 'root',
})
export class DeviceService {
  private readonly apiUrl = 'http://localhost:8080/api/devices';
  private readonly http = inject(HttpClient);

  findAll(): Observable<Device[]> {
    return this.http.get<Device[]>(this.apiUrl);
  }

  findById(id: number): Observable<Device> {
    return this.http.get<Device>(`${this.apiUrl}/${id}`);
  }

  create(data: DeviceRequest): Observable<Device> {
    return this.http.post<Device>(this.apiUrl, data);
  }

  update(id: number, data: DeviceRequest): Observable<Device> {
    return this.http.put<Device>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
