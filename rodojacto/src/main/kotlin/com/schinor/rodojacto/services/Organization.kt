package com.schinor.rodojacto.services

import com.schinor.rodojacto.dtos.OrganizationRequestDTO
import com.schinor.rodojacto.dtos.OrganizationResponseDTO
import com.schinor.rodojacto.exceptions.ResourceNotFoundException
import com.schinor.rodojacto.mappers.toResponseDTO
import com.schinor.rodojacto.models.AccessLevel
import com.schinor.rodojacto.models.Organization
import com.schinor.rodojacto.repositories.OrganizationRepository
import com.schinor.rodojacto.security.SecurityUtils
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrganizationService(
    private val repository: OrganizationRepository,
    private val securityUtils: SecurityUtils
) {

    @Transactional(readOnly = true)
    fun findAll(): List<OrganizationResponseDTO> {
        val currentUser = securityUtils.getCurrentUser()

        val list = if (currentUser.accessLevel == AccessLevel.MANAGER) {
            repository.findAll()
        } else {
            listOf(currentUser.organization)
        }

        return list.map { it.toResponseDTO() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): OrganizationResponseDTO {
        val organization = getEntityById(id)
        val currentUser = securityUtils.getCurrentUser()

        if (currentUser.accessLevel == AccessLevel.OPERATOR && organization.id != currentUser.organization.id) {
            throw AccessDeniedException("Você não tem permissão para visualizar esta organização.")
        }

        return organization.toResponseDTO()
    }

    @Transactional
    fun create(dto: OrganizationRequestDTO): OrganizationResponseDTO {
        val organization = Organization(
            corporateName = dto.corporateName,
            registrationCode = dto.registrationCode
        )
        return repository.save(organization).toResponseDTO()
    }

    @Transactional
    fun update(id: Long, dto: OrganizationRequestDTO): OrganizationResponseDTO {
        val organization = getEntityById(id)
        organization.corporateName = dto.corporateName
        organization.registrationCode = dto.registrationCode
        return repository.save(organization).toResponseDTO()
    }

    @Transactional
    fun delete(id: Long) {
        val organization = getEntityById(id)
        repository.delete(organization)
    }

    // Função auxiliar interna para buscar a entidade ou lançar exceção
    fun getEntityById(id: Long): Organization {
        return repository.findById(id).orElseThrow {
            ResourceNotFoundException("Organização com ID $id não encontrada.")
        }
    }
}