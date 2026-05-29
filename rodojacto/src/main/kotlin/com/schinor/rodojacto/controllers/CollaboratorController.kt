package com.schinor.rodojacto.controllers

import com.schinor.rodojacto.dtos.CollaboratorRequestDTO
import com.schinor.rodojacto.dtos.CollaboratorResponseDTO
import com.schinor.rodojacto.services.CollaboratorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/collaborators")
class CollaboratorController(
    private val service: CollaboratorService
) {

    @GetMapping
    fun findAll(): List<CollaboratorResponseDTO> = service.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): CollaboratorResponseDTO = service.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid dto: CollaboratorRequestDTO): CollaboratorResponseDTO = service.create(dto)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid dto: CollaboratorRequestDTO
    ): CollaboratorResponseDTO = service.update(id, dto)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = service.delete(id)
}