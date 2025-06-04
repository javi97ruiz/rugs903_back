package dev.javi.rugs_903_back.dto

data class ClientProfileDto(
    val id: Long,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val user: UserBasicDto,
    val address: DireccionDto
)




