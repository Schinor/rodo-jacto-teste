import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CollaboratorService } from '../../services/collaborator';
import { OrganizationService } from '../../../organizations/services/organization';
import { Organization } from '../../../organizations/models/organization.models';
import { AccessLevel, CollaboratorRequest } from '../../models/collaborator.models';
import { AuthService } from '../../../../features/auth/services/auth';

@Component({
  selector: 'app-collaborator-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './collaborator-form.html',
  styleUrl: './collaborator-form.css'
})
export class CollaboratorForm implements OnInit {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly collaboratorService = inject(CollaboratorService);
  private readonly organizationService = inject(OrganizationService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  protected readonly authService = inject(AuthService);

  collaboratorForm = this.fb.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: [''],
    accessLevel: [AccessLevel.OPERATOR, [Validators.required]],
    organizationId: [0, [Validators.required, Validators.min(1)]]
  });

  organizations = signal<Organization[]>([]);
  isEditMode = signal<boolean>(false);
  collaboratorId: number | null = null;
  isLoading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.loadOrganizations();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.collaboratorId = Number(id);
      this.loadCollaborator(this.collaboratorId);
      this.collaboratorForm.get('password')?.clearValidators();
    } else {
      this.collaboratorForm.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
    }
  }

  loadOrganizations(): void {
    this.organizationService.findAll().subscribe({
      next: (data) => {
        this.organizations.set(data);
        // Se for OPERATOR e estiver criando um novo, pré-seleciona a única organização disponível
        if (this.authService.isOperator() && !this.isEditMode() && data.length > 0) {
          this.collaboratorForm.patchValue({ organizationId: data[0].id });
        }
      },
      error: () => this.errorMessage.set('Erro ao carregar organizações.')
    });
  }

  loadCollaborator(id: number): void {
    this.isLoading.set(true);
    this.collaboratorService.findById(id).subscribe({
      next: (data) => {
        this.collaboratorForm.patchValue({
          fullName: data.fullName,
          email: data.email,
          accessLevel: data.accessLevel,
          organizationId: data.organizationId
        });
        this.isLoading.set(false);
      },
      error: (err) => {
        const msg = err.error?.message || 'Erro ao carregar dados do colaborador.';
        this.errorMessage.set(msg);
        this.isLoading.set(false);
      }
    });
  }

  onSubmit(): void {
    if (this.collaboratorForm.valid) {
      this.isLoading.set(true);
      const rawValues = this.collaboratorForm.getRawValue();
      
      const data: CollaboratorRequest = {
        fullName: rawValues.fullName,
        email: rawValues.email,
        accessLevel: rawValues.accessLevel as AccessLevel,
        organizationId: rawValues.organizationId,
        password: rawValues.password || undefined
      };

      const request = this.isEditMode() && this.collaboratorId
        ? this.collaboratorService.update(this.collaboratorId, data)
        : this.collaboratorService.create(data);

      request.subscribe({
        next: () => this.router.navigate(['/collaborators']),
        error: (err) => {
          const msg = err.error?.message || 'Erro ao salvar colaborador.';
          this.errorMessage.set(msg);
          this.isLoading.set(false);
        }
      });
    }
  }
}
