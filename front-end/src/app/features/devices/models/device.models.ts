export interface Device {
  id: number;
  model: string;
  assetTag: string;
  organizationId: number;
  createdAt?: string;
}

export interface DeviceRequest {
  model: string;
  assetTag: string;
  organizationId: number;
}
