package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.CustomProductRequestDto
import dev.javi.rugs_903_back.dto.CustomProductResponseDto
import dev.javi.rugs_903_back.mappers.toModel
import dev.javi.rugs_903_back.mappers.toResponseDto
import dev.javi.rugs_903_back.services.CustomProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/custom-products")
class CustomProductController(
    private val customProductService: CustomProductService
) {

    @GetMapping
    fun getAll(): List<CustomProductResponseDto> {
        return customProductService.findAll().map { it.toResponseDto() }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): CustomProductResponseDto {
        return customProductService.findById(id)?.toResponseDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Producto personalizado no encontrado")
    }

    @PostMapping
    fun create(@RequestBody dto: CustomProductRequestDto): CustomProductResponseDto {
        val saved = customProductService.save(dto.toModel())
        return saved.toResponseDto()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CustomProductRequestDto): CustomProductResponseDto {
        val updated = customProductService.update(id, dto.toModel())
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Producto personalizado no encontrado")
        return updated.toResponseDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        customProductService.deleteById(id)
    }
}
