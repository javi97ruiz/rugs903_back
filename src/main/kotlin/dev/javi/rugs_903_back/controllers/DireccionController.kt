package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.DireccionDto
import dev.javi.rugs_903_back.mappers.toModel
import dev.javi.rugs_903_back.mappers.toResponseDto
import dev.javi.rugs_903_back.mappers.toDtoList
import dev.javi.rugs_903_back.services.DireccionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/direcciones")
class DireccionController(
    private val direccionService: DireccionService
) {

    @GetMapping
    fun getAll(): List<DireccionDto> =
        direccionService.getAll().toDtoList()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): DireccionDto {
        return direccionService.getById(id)?.toResponseDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @PostMapping
    fun createDireccion(@RequestBody dto: DireccionDto): DireccionDto {
        return direccionService.save(dto.toModel()).toResponseDto()
    }

    @PutMapping("/{id}")
    fun updateDireccion(@PathVariable id: Long, @RequestBody dto: DireccionDto): DireccionDto {
        if (id != dto.id) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del path y del cuerpo no coinciden")
        }

        return direccionService.update(dto.toModel())?.toResponseDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun deleteDireccion(@PathVariable id: Long) {
        val direccion = direccionService.getById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        direccionService.deleteById(id)
    }
}
