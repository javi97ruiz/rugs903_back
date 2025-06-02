package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.ProductRequestDto
import dev.javi.rugs_903_back.dto.ProductResponseDto
import dev.javi.rugs_903_back.mappers.toModel
import dev.javi.rugs_903_back.mappers.toResponse
import dev.javi.rugs_903_back.mappers.toResponseList
import dev.javi.rugs_903_back.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getAll(): List<ProductResponseDto> =
        productService.getAll().toResponseList()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ProductResponseDto =
        productService.getById(id)?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado")

    @PostMapping
    fun create(@RequestBody dto: ProductRequestDto): ProductResponseDto {
        val saved = productService.create(dto.toModel())
        return saved.toResponse()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: ProductRequestDto): ProductResponseDto {
        val updated = productService.update(id, dto.toModel())
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado")
        return updated.toResponse()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        val exists = productService.getById(id)
        if (exists == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado")
        }
        productService.deleteById(id)
    }
}
