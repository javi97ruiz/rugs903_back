package dev.javi.rugs_903_back.dto

data class LoginResponse(
    val token: String,
    val username: String,
    val rol: String
)
