export interface OrganizationStat {
  name: string;
  count: number;
}

export interface DashboardStats {
  totalOrganizations: number;
  totalCollaborators: number;
  totalDevices: number;
  topOrganizationsByCollaborators: OrganizationStat[];
  topOrganizationsByDevices: OrganizationStat[];
}
