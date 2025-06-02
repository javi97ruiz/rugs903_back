package dev.javi.rugs_903_back.dto

data class UserResponseDto(
    val id: Long,
    val username: String,
    val rol: String,
    val isActive: Boolean
)
