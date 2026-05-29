import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { OrganizationService } from '../../services/organization';

@Component({
  selector: 'app-organization-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './organization-form.html',
  styleUrl: './organization-form.css'
})
export class OrganizationForm implements OnInit {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly organizationService = inject(OrganizationService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  organizationForm = this.fb.group({
    corporateName: ['', [Validators.required]],
    registrationCode: ['', [Validators.required]]
  });

  isEditMode = signal<boolean>(false);
  organizationId: number | null = null;
  isLoading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.organizationId = Number(id);
      this.loadOrganization(this.organizationId);
    }
  }

  loadOrganization(id: number): void {
    this.isLoading.set(true);
    this.organizationService.findById(id).subscribe({
      next: (data) => {
        this.organizationForm.patchValue(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Erro ao carregar dados da organização.');
        this.isLoading.set(false);
      }
    });
  }

  onSubmit(): void {
    if (this.organizationForm.valid) {
      this.isLoading.set(true);
      const data = this.organizationForm.getRawValue();

      const request = this.isEditMode() && this.organizationId
        ? this.organizationService.update(this.organizationId, data)
        : this.organizationService.create(data);

      request.subscribe({
        next: () => this.router.navigate(['/organizations']),
        error: () => {
          this.errorMessage.set('Erro ao salvar organização.');
          this.isLoading.set(false);
        }
      });
    }
  }
}
