package com.schinor.rodojacto.controllers

import com.schinor.rodojacto.dtos.DeviceRequestDTO
import com.schinor.rodojacto.dtos.DeviceResponseDTO
import com.schinor.rodojacto.services.DeviceService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/devices")
class DeviceController(
    private val service: DeviceService
) {

    @GetMapping
    fun findAll(): List<DeviceResponseDTO> = service.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): DeviceResponseDTO = service.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid dto: DeviceRequestDTO): DeviceResponseDTO = service.create(dto)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid dto: DeviceRequestDTO
    ): DeviceResponseDTO = service.update(id, dto)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = service.delete(id)
}