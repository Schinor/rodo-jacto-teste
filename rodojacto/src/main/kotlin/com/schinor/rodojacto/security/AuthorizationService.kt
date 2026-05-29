package com.schinor.rodojacto.security

import com.schinor.rodojacto.repositories.CollaboratorRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthorizationService(
    private val repository: CollaboratorRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val collaborator = repository.findByEmail(username)
            ?: throw UsernameNotFoundException("Utilizador não encontrado")

        return CollaboratorUserDetails(collaborator)
    }
}
