package dev.javi.rugs_903_back.dto

data class ProductRequestDto(
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imagen: String // ✅ NUEVO CAMPO
)

