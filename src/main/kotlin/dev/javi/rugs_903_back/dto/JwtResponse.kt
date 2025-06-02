package dev.javi.rugs_903_back.dto

data class JwtResponse(
    val token: String,
    val id: Long,
    val username: String,
    val rol: String
)
