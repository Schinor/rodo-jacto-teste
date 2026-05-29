package com.schinor.rodojacto.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.schinor.rodojacto.models.Collaborator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService {

    @Value("\${api.security.token.secret:my-super-secret-key-rodojacto}")
    private lateinit var secret: String

    fun generateToken(collaborator: Collaborator): String {
        return try {
            val algorithm = Algorithm.HMAC256(secret)
            JWT.create()
                .withIssuer("rodojacto-api")
                .withSubject(collaborator.email)
                .withClaim("role", collaborator.accessLevel.name)
                .withClaim("organizationId", collaborator.organization.id) // Guardamos a organização para usar nos filtros do OPERATOR depois
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm)
        } catch (exception: JWTCreationException) {
            throw RuntimeException("Erro ao gerar token JWT", exception)
        }
    }

    fun validateToken(token: String): String {
        return try {
            val algorithm = Algorithm.HMAC256(secret)
            JWT.require(algorithm)
                .withIssuer("rodojacto-api")
                .build()
                .verify(token)
                .subject
        } catch (exception: JWTVerificationException) {
            "" // Retorna string vazia se o token for inválido
        }
    }

    private fun generateExpirationDate(): Instant {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"))
    }
}
