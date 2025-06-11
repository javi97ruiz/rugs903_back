// dto/CustomProductRequestDto.kt
package dev.javi.rugs_903_back.dto

data class CustomProductRequestDto(
    val name: String,
    val height: Int,
    val length: Int,
    val imageUrl: String,
    val price: Double
)
