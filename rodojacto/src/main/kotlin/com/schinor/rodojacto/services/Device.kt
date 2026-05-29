package com.schinor.rodojacto.services

import com.schinor.rodojacto.dtos.DeviceRequestDTO
import com.schinor.rodojacto.dtos.DeviceResponseDTO
import com.schinor.rodojacto.exceptions.ResourceNotFoundException
import com.schinor.rodojacto.mappers.toResponseDTO
import com.schinor.rodojacto.models.AccessLevel
import com.schinor.rodojacto.models.Device
import com.schinor.rodojacto.repositories.DeviceRepository
import com.schinor.rodojacto.security.SecurityUtils
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeviceService(
    private val repository: DeviceRepository,
    private val organizationService: OrganizationService,
    private val securityUtils: SecurityUtils
) {

    @Transactional(readOnly = true)
    fun findAll(): List<DeviceResponseDTO> {
        val currentUser = securityUtils.getCurrentUser()

        val list = if (currentUser.accessLevel == AccessLevel.MANAGER) {
            repository.findAll()
        } else {
            repository.findByOrganizationId(currentUser.organization.id!!)
        }

        return list.map { it.toResponseDTO() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): DeviceResponseDTO {
        val device = getEntityById(id)
        val currentUser = securityUtils.getCurrentUser()

        if (currentUser.accessLevel == AccessLevel.OPERATOR && device.organization.id != currentUser.organization.id) {
            throw AccessDeniedException("Você não tem permissão para visualizar este dispositivo.")
        }

        return device.toResponseDTO()
    }

    @Transactional
    fun create(dto: DeviceRequestDTO): DeviceResponseDTO {
        val organization = organizationService.getEntityById(dto.organizationId)

        val device = Device(
            model = dto.model,
            assetTag = dto.assetTag,
            organization = organization
        )
        return repository.save(device).toResponseDTO()
    }

    @Transactional
    fun update(id: Long, dto: DeviceRequestDTO): DeviceResponseDTO {
        val device = getEntityById(id)
        val organization = organizationService.getEntityById(dto.organizationId)

        device.model = dto.model
        device.assetTag = dto.assetTag
        device.organization = organization

        return repository.save(device).toResponseDTO()
    }

    @Transactional
    fun delete(id: Long) {
        val device = getEntityById(id)
        repository.delete(device)
    }

    private fun getEntityById(id: Long): Device {
        return repository.findById(id).orElseThrow {
            ResourceNotFoundException("Dispositivo com ID $id não encontrado.")
        }
    }
}