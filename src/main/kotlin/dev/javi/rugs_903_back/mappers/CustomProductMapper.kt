// mappers/CustomProductMapper.kt
package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.CustomProductRequestDto
import dev.javi.rugs_903_back.dto.CustomProductResponseDto
import dev.javi.rugs_903_back.models.CustomProduct

fun CustomProduct.toResponseDto(): CustomProductResponseDto = CustomProductResponseDto(
    id = this.id,
    name = this.name,
    height = this.height,
    length = this.length,
    imageUrl = this.imageUrl,
    price = this.price,     // AÑADIMOS
    pedidoId = this.pedido?.id
)

fun CustomProductRequestDto.toModel(): CustomProduct = CustomProduct(
    name = this.name,
    height = this.height,
    length = this.length,
    imageUrl = this.imageUrl,
    price = this.price      // AÑADIMOS
)

