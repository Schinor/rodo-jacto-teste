import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../services/dashboard';
import { DashboardStats } from '../../models/dashboard.models';
import { AuthService } from '../../../../features/auth/services/auth';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-home.html',
  styleUrls: ['./dashboard-home.css']
})
export class DashboardHome implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  protected readonly authService = inject(AuthService);
  
  // Usando Signals para garantir que o Angular detecte a mudança de estado imediatamente
  stats = signal<DashboardStats | null>(null);
  loading = signal<boolean>(true);

  ngOnInit(): void {
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Erro ao buscar estatísticas', err);
        this.loading.set(false);
      }
    });
  }
}
