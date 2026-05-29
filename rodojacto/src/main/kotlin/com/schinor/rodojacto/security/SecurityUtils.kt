package com.schinor.rodojacto.security

import com.schinor.rodojacto.models.Collaborator
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtils {
    fun getCurrentUser(): Collaborator {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw RuntimeException("Usuário não autenticado.")
        val principal = authentication.principal as CollaboratorUserDetails
        return principal.collaborator
    }
}
