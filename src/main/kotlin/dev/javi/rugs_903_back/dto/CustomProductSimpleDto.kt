package dev.javi.rugs_903_back.dto

data class CustomProductSimpleDto(
    val id: Long,
    val name: String,
    val height: Int,
    val length: Int,
    val imageUrl: String
)
