package com.schinor.rodojacto.controllers

import com.schinor.rodojacto.dtos.DashboardDTO
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
        return DashboardDTO(
            totalOrganizations = organizationRepository.count(),
            totalCollaborators = collaboratorRepository.count(),
            totalDevices = deviceRepository.count()
        )
    }
}
