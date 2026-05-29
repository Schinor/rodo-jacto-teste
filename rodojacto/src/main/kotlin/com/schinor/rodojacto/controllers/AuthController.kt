package com.schinor.rodojacto.controllers

import com.schinor.rodojacto.dtos.LoginRequestDTO
import com.schinor.rodojacto.dtos.LoginResponseDTO
import com.schinor.rodojacto.security.CollaboratorUserDetails
import com.schinor.rodojacto.security.TokenService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController 
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService
) {

    @PostMapping("/login")
    fun login(@RequestBody @Valid data: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val usernamePassword = UsernamePasswordAuthenticationToken(data.email, data.password)
        val auth = authenticationManager.authenticate(usernamePassword)

        val userDetails = auth.principal as CollaboratorUserDetails
        val token = tokenService.generateToken(userDetails.collaborator)

        return ResponseEntity.ok(LoginResponseDTO(token))
    }
}
