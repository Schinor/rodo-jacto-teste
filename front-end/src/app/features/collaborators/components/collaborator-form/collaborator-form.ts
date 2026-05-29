import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CollaboratorService } from '../../services/collaborator';
import { OrganizationService } from '../../../organizations/services/organization';
import { Organization } from '../../../organizations/models/organization.models';
import { AccessLevel, CollaboratorRequest } from '../../models/collaborator.models';

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

  collaboratorForm = this.fb.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: [''],
    accessLevel: [AccessLevel.OPERATOR, [Validators.required]],
    organizationId: [0, [Validators.required, Validators.min(1)]]
  });

  organizations: Organization[] = [];
  isEditMode = false;
  collaboratorId: number | null = null;
  isLoading = false;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadOrganizations();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.collaboratorId = Number(id);
      this.loadCollaborator(this.collaboratorId);
      this.collaboratorForm.get('password')?.clearValidators();
    } else {
      this.collaboratorForm.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
    }
  }

  loadOrganizations(): void {
    this.organizationService.findAll().subscribe({
      next: (data) => this.organizations = data,
      error: () => this.errorMessage = 'Erro ao carregar organizações.'
    });
  }

  loadCollaborator(id: number): void {
    this.isLoading = true;
    this.collaboratorService.findById(id).subscribe({
      next: (data) => {
        this.collaboratorForm.patchValue({
          fullName: data.fullName,
          email: data.email,
          accessLevel: data.accessLevel,
          organizationId: data.organizationId
        });
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar dados do colaborador.';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.collaboratorForm.valid) {
      this.isLoading = true;
      const rawValues = this.collaboratorForm.getRawValue();
      
      const data: CollaboratorRequest = {
        fullName: rawValues.fullName,
        email: rawValues.email,
        accessLevel: rawValues.accessLevel as AccessLevel,
        organizationId: rawValues.organizationId,
        password: rawValues.password || undefined
      };

      const request = this.isEditMode && this.collaboratorId
        ? this.collaboratorService.update(this.collaboratorId, data)
        : this.collaboratorService.create(data);

      request.subscribe({
        next: () => this.router.navigate(['/collaborators']),
        error: () => {
          this.errorMessage = 'Erro ao salvar colaborador.';
          this.isLoading = false;
        }
      });
    }
  }
}
