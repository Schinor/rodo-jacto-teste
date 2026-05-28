package com.schinor.rodojacto.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

// O que a API RECEBE ao cadastrar um dispositivo
data class DeviceRequestDTO(
    @field:NotBlank(message = "O modelo é obrigatório")
    val model: String,

    @field:NotBlank(message = "A tag do ativo é obrigatória")
    val assetTag: String,

    @field:NotNull(message = "O ID da organização é obrigatório")
    val organizationId: Long
)

// O que a API DEVOLVE
data class DeviceResponseDTO(
    val id: Long,
    val model: String,
    val assetTag: String,
    val organizationId: Long,
    val createdAt: LocalDateTime?
)