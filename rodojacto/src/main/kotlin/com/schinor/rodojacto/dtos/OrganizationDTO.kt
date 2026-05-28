package com.schinor.rodojacto.dtos

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

// O que a API RECEBE do Angular ao criar/atualizar
data class OrganizationRequestDTO(
    @field:NotBlank(message = "A razão social é obrigatória")
    val corporateName: String,

    @field:NotBlank(message = "O código de registro é obrigatório")
    val registrationCode: String
)

// O que a API DEVOLVE para o Angular
data class OrganizationResponseDTO(
    val id: Long,
    val corporateName: String,
    val registrationCode: String,
    val createdAt: LocalDateTime?
)