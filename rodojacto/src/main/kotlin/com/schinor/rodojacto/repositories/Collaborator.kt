package com.schinor.rodojacto.repositories

import com.schinor.rodojacto.models.Collaborator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CollaboratorRepository : JpaRepository<Collaborator, Long> {
    fun findByEmail(email: String): Collaborator?
    fun findByOrganizationId(organizationId: Long): List<Collaborator>
}
