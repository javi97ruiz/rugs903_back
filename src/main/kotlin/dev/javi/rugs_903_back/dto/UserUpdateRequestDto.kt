// UserUpdateRequest.kt
package dev.javi.rugs_903_back.dto

data class UserUpdateRequestDto(
    val username: String,
    val rol: String,
    val password: String? = null
)
