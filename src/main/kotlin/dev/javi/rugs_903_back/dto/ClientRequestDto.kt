// dto/ClientRequestDto.kt
package dev.javi.rugs_903_back.dto

data class ClientRequestDto(
    val phoneNumber: String,
    val name: String,
    val surname: String,
    val addressId: Long, // asumimos que la dirección se ha creado antes
    val userId: Long
)
