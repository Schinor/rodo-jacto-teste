package com.schinor.rodojacto.security

import com.schinor.rodojacto.models.Collaborator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CollaboratorUserDetails(val collaborator: Collaborator) : UserDetails {

    // Define se o utilizador é MANAGER ou OPERATOR para o Spring Security
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${collaborator.accessLevel.name}"))
    }

    override fun getPassword(): String = collaborator.password
    override fun getUsername(): String = collaborator.email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true

    // Se isDeleted for true (Soft Delete), a conta fica desativada
    override fun isEnabled(): Boolean = !collaborator.isDeleted
}
