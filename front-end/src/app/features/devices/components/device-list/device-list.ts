import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { DeviceService } from '../../services/device';
import { Device } from '../../models/device.models';

@Component({
  selector: 'app-device-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './device-list.html',
  styleUrl: './device-list.css'
})
export class DeviceList implements OnInit, OnDestroy {
  private readonly deviceService = inject(DeviceService);
  private readonly router = inject(Router);
  private subscription = new Subscription();
  
  devices: Device[] = [];
  isLoading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadDevices();

    // Sincronização Instantânea: Recarrega os dados sempre que a rota é ativada
    this.subscription.add(
      this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
      ).subscribe(() => {
        this.loadDevices();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  loadDevices(): void {
    this.isLoading = true;
    this.deviceService.findAll().subscribe({
      next: (data) => {
        this.devices = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar dispositivos.';
        this.isLoading = false;
      }
    });
  }

  onDelete(id: number): void {
    if (confirm('Tem certeza que deseja excluir este dispositivo?')) {
      this.deviceService.delete(id).subscribe({
        next: () => this.loadDevices(),
        error: () => alert('Erro ao excluir dispositivo.')
      });
    }
  }
}
