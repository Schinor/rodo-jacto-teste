package com.schinor.rodojacto.controllers

import com.schinor.rodojacto.dtos.OrganizationRequestDTO
import com.schinor.rodojacto.dtos.OrganizationResponseDTO
import com.schinor.rodojacto.services.OrganizationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/organizations")
class OrganizationController(
    private val service: OrganizationService
) {

    @GetMapping
    fun findAll(): List<OrganizationResponseDTO> = service.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): OrganizationResponseDTO = service.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid dto: OrganizationRequestDTO): OrganizationResponseDTO = service.create(dto)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid dto: OrganizationRequestDTO
    ): OrganizationResponseDTO = service.update(id, dto)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = service.delete(id)
}