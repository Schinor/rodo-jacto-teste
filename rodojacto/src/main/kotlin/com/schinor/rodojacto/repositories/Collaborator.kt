package com.schinor.rodojacto.repositories

import com.schinor.rodojacto.models.Collaborator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CollaboratorRepository : JpaRepository<Collaborator, Long> {
    // Este método será fundamental para o Spring Security e JWT
    // Ele buscará o colaborador no banco na hora do login
    fun findByEmail(email: String): Collaborator?
}