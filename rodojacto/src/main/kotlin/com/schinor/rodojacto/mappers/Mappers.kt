package com.schinor.rodojacto.mappers

import com.schinor.rodojacto.dtos.*
import com.schinor.rodojacto.models.*

fun Organization.toResponseDTO() = OrganizationResponseDTO(
    id = this.id!!,
    corporateName = this.corporateName,
    registrationCode = this.registrationCode,
    createdAt = this.createdAt
)

// A senha não é devolvida no DTO por segurança
fun Collaborator.toResponseDTO() = CollaboratorResponseDTO(
    id = this.id!!,
    fullName = this.fullName,
    email = this.email,
    accessLevel = this.accessLevel,
    organizationId = this.organization.id!!,
    createdAt = this.createdAt
)

fun Device.toResponseDTO() = DeviceResponseDTO(
    id = this.id!!,
    model = this.model,
    assetTag = this.assetTag,
    organizationId = this.organization.id!!,
    createdAt = this.createdAt
)