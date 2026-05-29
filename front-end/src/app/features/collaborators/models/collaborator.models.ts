export enum AccessLevel {
  MANAGER = 'MANAGER',
  OPERATOR = 'OPERATOR'
}

export interface Collaborator {
  id: number;
  fullName: string;
  email: string;
  accessLevel: AccessLevel;
  organizationId: number;
  createdAt?: string;
}

export interface CollaboratorRequest {
  fullName: string;
  email: string;
  password?: string;
  accessLevel: AccessLevel;
  organizationId: number;
}
