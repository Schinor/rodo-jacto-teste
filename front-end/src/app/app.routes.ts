import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { authGuard } from './core/guards/auth-guard';
import { OrganizationList } from './features/organizations/components/organization-list/organization-list';
import { OrganizationForm } from './features/organizations/components/organization-form/organization-form';
import { CollaboratorList } from './features/collaborators/components/collaborator-list/collaborator-list';
import { CollaboratorForm } from './features/collaborators/components/collaborator-form/collaborator-form';
import { DeviceList } from './features/devices/components/device-list/device-list';
import { DeviceForm } from './features/devices/components/device-form/device-form';

export const routes: Routes = [
  { path: 'login', component: Login },
  { 
    path: '', 
    canActivate: [authGuard], 
    children: [
      { path: 'organizations', component: OrganizationList },
      { path: 'organizations/new', component: OrganizationForm },
      { path: 'organizations/edit/:id', component: OrganizationForm },
      
      { path: 'collaborators', component: CollaboratorList },
      { path: 'collaborators/new', component: CollaboratorForm },
      { path: 'collaborators/edit/:id', component: CollaboratorForm },

      { path: 'devices', component: DeviceList },
      { path: 'devices/new', component: DeviceForm },
      { path: 'devices/edit/:id', component: DeviceForm },
      
      { path: '', redirectTo: 'organizations', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];
