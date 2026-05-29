import { Component, inject, OnInit, OnDestroy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { CollaboratorService } from '../../services/collaborator';
import { Collaborator } from '../../models/collaborator.models';

@Component({
  selector: 'app-collaborator-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './collaborator-list.html',
  styleUrl: './collaborator-list.css'
})
export class CollaboratorList implements OnInit, OnDestroy {
  private readonly collaboratorService = inject(CollaboratorService);
  private readonly router = inject(Router);
  private subscription = new Subscription();
  
  collaborators = signal<Collaborator[]>([]);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.loadCollaborators();

    // Sincronização Instantânea: Recarrega os dados sempre que a rota é ativada
    this.subscription.add(
      this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
      ).subscribe(() => {
        this.loadCollaborators();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  loadCollaborators(): void {
    this.isLoading.set(true);
    this.collaboratorService.findAll().subscribe({
      next: (data) => {
        this.collaborators.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Erro ao carregar colaboradores.');
        this.isLoading.set(false);
      }
    });
  }

  onDelete(id: number): void {
    if (confirm('Tem certeza que deseja excluir este colaborador?')) {
      this.collaboratorService.delete(id).subscribe({
        next: () => this.loadCollaborators(),
        error: () => alert('Erro ao excluir colaborador.')
      });
    }
  }
}
