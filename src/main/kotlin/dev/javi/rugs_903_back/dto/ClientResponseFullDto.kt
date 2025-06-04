package dev.javi.rugs_903_back.dto

data class ClientResponseFullDto(
    val id: Long,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val user: UserResponseDto,
    val address: DireccionDto
)
