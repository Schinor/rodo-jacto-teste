package com.schinor.rodojacto.controllers

import com.schinor.rodojacto.dtos.DashboardDTO
import com.schinor.rodojacto.dtos.OrganizationStatDTO
import com.schinor.rodojacto.repositories.CollaboratorRepository
import com.schinor.rodojacto.repositories.DeviceRepository
import com.schinor.rodojacto.repositories.OrganizationRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val organizationRepository: OrganizationRepository,
    private val collaboratorRepository: CollaboratorRepository,
    private val deviceRepository: DeviceRepository
) {

    @GetMapping("/stats")
    fun getStats(): DashboardDTO {
        val organizations = organizationRepository.findAll()
        
        val topByCollaborators = organizations.map { org ->
            OrganizationStatDTO(org.corporateName, collaboratorRepository.findByOrganizationId(org.id!!).size.toLong())
        }.sortedByDescending { it.count }.take(5)

        val topByDevices = organizations.map { org ->
            OrganizationStatDTO(org.corporateName, deviceRepository.findByOrganizationId(org.id!!).size.toLong())
        }.sortedByDescending { it.count }.take(5)

        return DashboardDTO(
            totalOrganizations = organizationRepository.count(),
            totalCollaborators = collaboratorRepository.count(),
            totalDevices = deviceRepository.count(),
            topOrganizationsByCollaborators = topByCollaborators,
            topOrganizationsByDevices = topByDevices
        )
    }
}
