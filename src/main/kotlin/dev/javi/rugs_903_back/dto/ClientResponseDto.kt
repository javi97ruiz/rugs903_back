// dto/ClientResponseDto.kt
package dev.javi.rugs_903_back.dto

data class ClientResponseDto(
    val id: Long,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val userId: Long,
    val addressId: Long,
    val isActive: Boolean

)

