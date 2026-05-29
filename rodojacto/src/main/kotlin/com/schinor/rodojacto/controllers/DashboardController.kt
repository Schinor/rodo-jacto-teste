package com.schinor.rodojacto.controllers

import com.schinor.rodojacto.dtos.DashboardDTO
import com.schinor.rodojacto.dtos.OrganizationStatDTO
import com.schinor.rodojacto.models.AccessLevel
import com.schinor.rodojacto.repositories.CollaboratorRepository
import com.schinor.rodojacto.repositories.DeviceRepository
import com.schinor.rodojacto.repositories.OrganizationRepository
import com.schinor.rodojacto.security.SecurityUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val organizationRepository: OrganizationRepository,
    private val collaboratorRepository: CollaboratorRepository,
    private val deviceRepository: DeviceRepository,
    private val securityUtils: SecurityUtils
) {

    @GetMapping("/stats")
    fun getStats(): DashboardDTO {
        val currentUser = securityUtils.getCurrentUser()
        
        return if (currentUser.accessLevel == AccessLevel.MANAGER) {
            val organizations = organizationRepository.findAll()
            
            val topByCollaborators = organizations.map { org ->
                OrganizationStatDTO(org.corporateName, collaboratorRepository.findByOrganizationId(org.id!!).size.toLong())
            }.sortedByDescending { it.count }.take(5)

            val topByDevices = organizations.map { org ->
                OrganizationStatDTO(org.corporateName, deviceRepository.findByOrganizationId(org.id!!).size.toLong())
            }.sortedByDescending { it.count }.take(5)

            DashboardDTO(
                totalOrganizations = organizationRepository.count(),
                totalCollaborators = collaboratorRepository.count(),
                totalDevices = deviceRepository.count(),
                topOrganizationsByCollaborators = topByCollaborators,
                topOrganizationsByDevices = topByDevices
            )
        } else {
            // Se for OPERATOR, vê apenas os seus próprios números e não vê rankings globais
            val orgId = currentUser.organization.id!!
            DashboardDTO(
                totalOrganizations = 1,
                totalCollaborators = collaboratorRepository.findByOrganizationId(orgId).size.toLong(),
                totalDevices = deviceRepository.findByOrganizationId(orgId).size.toLong(),
                topOrganizationsByCollaborators = emptyList(),
                topOrganizationsByDevices = emptyList()
            )
        }
    }
}
