package com.schinor.rodojacto.repositories

import com.schinor.rodojacto.models.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : JpaRepository<Organization, Long> {
    // Você pode adicionar métodos customizados aqui se precisar no futuro,
    // como buscar por registrationCode
    fun findByRegistrationCode(registrationCode: String): Organization?
}