import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../services/dashboard';
import { DashboardStats } from '../../models/dashboard.models';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-home.html',
  styleUrls: ['./dashboard-home.css']
})
export class DashboardHome implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  stats: DashboardStats | null = null;

  ngOnInit(): void {
    this.dashboardService.getStats().subscribe({
      next: (data) => this.stats = data,
      error: (err) => console.error('Erro ao buscar estatísticas', err)
    });
  }
}
