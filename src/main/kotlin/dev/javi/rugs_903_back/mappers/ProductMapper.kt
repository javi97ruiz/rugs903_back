package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.ProductRequestDto
import dev.javi.rugs_903_back.dto.ProductResponseDto
import dev.javi.rugs_903_back.models.Product

fun Product.toResponse(): ProductResponseDto = ProductResponseDto(
    id = this.id,
    name = this.name,
    description = this.description,
    price = this.price,
    quantity = this.quantity,
    imagen = this.imagen,
    isActive = this.isActive
)



fun ProductRequestDto.toModel(): Product = Product(
    name = this.name,
    description = this.description,
    price = this.price,
    quantity = this.quantity,
    imagen = this.imagen // ✅ NUEVO CAMPO
)


fun List<Product>.toResponseList(): List<ProductResponseDto> = this.map { it.toResponse() }
