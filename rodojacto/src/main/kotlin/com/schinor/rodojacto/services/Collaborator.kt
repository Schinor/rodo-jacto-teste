package com.schinor.rodojacto.services

import com.schinor.rodojacto.dtos.CollaboratorRequestDTO
import com.schinor.rodojacto.dtos.CollaboratorResponseDTO
import com.schinor.rodojacto.exceptions.ResourceNotFoundException
import com.schinor.rodojacto.mappers.toResponseDTO
import com.schinor.rodojacto.models.AccessLevel
import com.schinor.rodojacto.models.Collaborator
import com.schinor.rodojacto.repositories.CollaboratorRepository
import com.schinor.rodojacto.security.SecurityUtils
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CollaboratorService(
    private val repository: CollaboratorRepository,
    private val organizationService: OrganizationService,
    private val passwordEncoder: PasswordEncoder,
    private val securityUtils: SecurityUtils
) {

    @Transactional(readOnly = true)
    fun findAll(): List<CollaboratorResponseDTO> {
        val currentUser = securityUtils.getCurrentUser()

        // Regra: MANAGER vê tudo, OPERATOR vê apenas da sua organização
        val list = if (currentUser.accessLevel == AccessLevel.MANAGER) {
            repository.findAll()
        } else {
            repository.findByOrganizationId(currentUser.organization.id!!)
        }

        return list.map { it.toResponseDTO() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): CollaboratorResponseDTO {
        val collaborator = getEntityById(id)
        val currentUser = securityUtils.getCurrentUser()

        // Bloqueia o acesso se for OPERATOR tentando ver dados de outra organização
        if (currentUser.accessLevel == AccessLevel.OPERATOR && collaborator.organization.id != currentUser.organization.id) {
            throw AccessDeniedException("Você não tem permissão para visualizar este colaborador.")
        }

        return collaborator.toResponseDTO()
    }

    @Transactional
    fun create(dto: CollaboratorRequestDTO): CollaboratorResponseDTO {
        val organization = organizationService.getEntityById(dto.organizationId)

        val collaborator = Collaborator(
            fullName = dto.fullName,
            email = dto.email,
            password = passwordEncoder.encode(dto.password)!!,
            accessLevel = dto.accessLevel,
            organization = organization
        )
        return repository.save(collaborator).toResponseDTO()
    }

    @Transactional
    fun update(id: Long, dto: CollaboratorRequestDTO): CollaboratorResponseDTO {
        val collaborator = getEntityById(id)
        val organization = organizationService.getEntityById(dto.organizationId)

        collaborator.fullName = dto.fullName
        collaborator.email = dto.email
        // collaborator.password = dto.password (Atualização de senha costuma ser um endpoint separado)
        collaborator.accessLevel = dto.accessLevel
        collaborator.organization = organization

        return repository.save(collaborator).toResponseDTO()
    }

    @Transactional
    fun delete(id: Long) {
        val collaborator = getEntityById(id)
        repository.delete(collaborator)
    }

    private fun getEntityById(id: Long): Collaborator {
        return repository.findById(id).orElseThrow {
            ResourceNotFoundException("Colaborador com ID $id não encontrado.")
        }
    }
}