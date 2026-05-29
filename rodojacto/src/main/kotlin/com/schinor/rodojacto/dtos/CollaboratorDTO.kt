package com.schinor.rodojacto.dtos

import com.schinor.rodojacto.models.AccessLevel
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

// O que a API RECEBE ao criar/atualizar um colaborador
data class CollaboratorRequestDTO(
    @field:NotBlank(message = "O nome completo é obrigatório")
    val fullName: String,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "Formato de e-mail inválido")
    val email: String,

    // Senha opcional para permitir atualizações sem reenviar a senha
    val password: String? = null,

    @field:NotNull(message = "O nível de acesso é obrigatório")
    val accessLevel: AccessLevel,

    @field:NotNull(message = "O ID da organização é obrigatório")
    val organizationId: Long
)

// O que a API DEVOLVE (sem a senha!)
data class CollaboratorResponseDTO(
    val id: Long,
    val fullName: String,
    val email: String,
    val accessLevel: AccessLevel,
    val organizationId: Long, // Devolvemos o ID para facilitar a vinculação no front-end
    val createdAt: LocalDateTime?
)
