package com.schinor.rodojacto.dtos

data class OrganizationStatDTO(
    val name: String,
    val count: Long
)

data class DashboardDTO(
    val totalOrganizations: Long,
    val totalCollaborators: Long,
    val totalDevices: Long,
    val topOrganizationsByCollaborators: List<OrganizationStatDTO>,
    val topOrganizationsByDevices: List<OrganizationStatDTO>
)
