import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DeviceService } from '../../services/device';
import { OrganizationService } from '../../../organizations/services/organization';
import { Organization } from '../../../organizations/models/organization.models';
import { DeviceRequest } from '../../models/device.models';
import { AuthService } from '../../../../features/auth/services/auth';

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
  protected readonly authService = inject(AuthService);

  deviceForm = this.fb.group({
    model: ['', [Validators.required]],
    assetTag: ['', [Validators.required]],
    organizationId: [0, [Validators.required, Validators.min(1)]]
  });

  organizations = signal<Organization[]>([]);
  isEditMode = signal<boolean>(false);
  deviceId: number | null = null;
  isLoading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.loadOrganizations();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.deviceId = Number(id);
      this.loadDevice(this.deviceId);
    }
  }

  loadOrganizations(): void {
    this.organizationService.findAll().subscribe({
      next: (data) => {
        this.organizations.set(data);
        if (this.authService.isOperator() && !this.isEditMode() && data.length > 0) {
          this.deviceForm.patchValue({ organizationId: data[0].id });
        }
      },
      error: () => this.errorMessage.set('Erro ao carregar organizações.')
    });
  }

  loadDevice(id: number): void {
    this.isLoading.set(true);
    this.deviceService.findById(id).subscribe({
      next: (data) => {
        this.deviceForm.patchValue({
          model: data.model,
          assetTag: data.assetTag,
          organizationId: data.organizationId
        });
        this.isLoading.set(false);
      },
      error: (err) => {
        const msg = err.error?.message || 'Erro ao carregar dados do dispositivo.';
        this.errorMessage.set(msg);
        this.isLoading.set(false);
      }
    });
  }

  onSubmit(): void {
    if (this.deviceForm.valid) {
      this.isLoading.set(true);
      const data: DeviceRequest = this.deviceForm.getRawValue();

      const request = this.isEditMode() && this.deviceId
        ? this.deviceService.update(this.deviceId, data)
        : this.deviceService.create(data);

      request.subscribe({
        next: () => this.router.navigate(['/devices']),
        error: (err) => {
          const msg = err.error?.message || 'Erro ao salvar dispositivo.';
          this.errorMessage.set(msg);
          this.isLoading.set(false);
        }
      });
    }
  }
}
