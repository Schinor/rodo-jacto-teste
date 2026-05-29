import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DeviceService } from '../../services/device';
import { OrganizationService } from '../../../organizations/services/organization';
import { Organization } from '../../../organizations/models/organization.models';

@Component({
  selector: 'app-device-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './device-form.html',
  styleUrl: './device-form.css'
})
export class DeviceForm implements OnInit {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly deviceService = inject(DeviceService);
  private readonly organizationService = inject(OrganizationService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  deviceForm = this.fb.group({
    model: ['', [Validators.required]],
    assetTag: ['', [Validators.required]],
    organizationId: [0, [Validators.required, Validators.min(1)]]
  });

  organizations: Organization[] = [];
  isEditMode = false;
  deviceId: number | null = null;
  isLoading = false;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadOrganizations();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.deviceId = Number(id);
      this.loadDevice(this.deviceId);
    }
  }

  loadOrganizations(): void {
    this.organizationService.findAll().subscribe({
      next: (data) => this.organizations = data,
      error: () => this.errorMessage = 'Erro ao carregar organizações.'
    });
  }

  loadDevice(id: number): void {
    this.isLoading = true;
    this.deviceService.findById(id).subscribe({
      next: (data) => {
        this.deviceForm.patchValue(data);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar dados do dispositivo.';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.deviceForm.valid) {
      this.isLoading = true;
      const data = this.deviceForm.getRawValue();

      const request = this.isEditMode && this.deviceId
        ? this.deviceService.update(this.deviceId, data)
        : this.deviceService.create(data);

      request.subscribe({
        next: () => this.router.navigate(['/devices']),
        error: () => {
          this.errorMessage = 'Erro ao salvar dispositivo.';
          this.isLoading = false;
        }
      });
    }
  }
}
