package dev.javi.rugs_903_back.dto

data class UserRequestDto(
    val username: String,
    val email: String,
    val password: String,
    val rol: String
)
