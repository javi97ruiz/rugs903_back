package dev.javi.rugs_903_back.dto

data class ProductResponseDto(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imagen: String, // Puede ser una URL o base64, depende cómo la almacenes
    val isActive: Boolean

)

