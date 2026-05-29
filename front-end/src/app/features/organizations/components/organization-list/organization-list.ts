import { Component, inject, OnInit, OnDestroy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { OrganizationService } from '../../services/organization';
import { Organization } from '../../models/organization.models';

@Component({
  selector: 'app-organization-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './organization-list.html',
  styleUrl: './organization-list.css'
})
export class OrganizationList implements OnInit, OnDestroy {
  private readonly organizationService = inject(OrganizationService);
  private readonly router = inject(Router);
  private subscription = new Subscription();
  
  organizations = signal<Organization[]>([]);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.loadOrganizations();

    // Sincronização Instantânea: Recarrega os dados sempre que a rota é ativada
    this.subscription.add(
      this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
      ).subscribe(() => {
        this.loadOrganizations();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  loadOrganizations(): void {
    this.isLoading.set(true);
    this.organizationService.findAll().subscribe({
      next: (data) => {
        this.organizations.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Erro ao carregar organizações.');
        this.isLoading.set(false);
      }
    });
  }

  onDelete(id: number): void {
    if (confirm('Tem certeza que deseja excluir esta organização?')) {
      this.organizationService.delete(id).subscribe({
        next: () => this.loadOrganizations(),
        error: () => alert('Erro ao excluir organização.')
      });
    }
  }
}
