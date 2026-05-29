export interface Organization {
  id: number;
  corporateName: string;
  registrationCode: string;
  createdAt?: string;
}

export interface OrganizationRequest {
  corporateName: string;
  registrationCode: string;
}
