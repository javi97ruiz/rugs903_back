package dev.javi.rugs_903_back.dto

data class DireccionDto(
    val id: Long,
    val calle: String,
    val numero: String,
    val portal: String,
    val piso: String,
    val codigoPostal: String,
    val ciudad: String,
    val provincia: String
)