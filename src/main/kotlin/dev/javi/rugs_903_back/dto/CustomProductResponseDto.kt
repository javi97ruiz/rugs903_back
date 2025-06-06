// dto/CustomProductResponseDto.kt
package dev.javi.rugs_903_back.dto

data class CustomProductResponseDto(
    val id: Long,
    val name: String,
    val height: Int,
    val length: Int,
    val imageUrl: String,
    val pedidoId: Long?
)
