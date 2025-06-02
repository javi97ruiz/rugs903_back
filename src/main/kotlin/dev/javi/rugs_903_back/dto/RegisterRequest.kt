package dev.javi.rugs_903_back.dto

data class RegisterRequest(
    val username: String,
    val password: String,
    val rol: String // "usuario" o "admin"
)
