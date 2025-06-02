// mappers/ClientMapper.kt
package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.ClientRequestDto
import dev.javi.rugs_903_back.dto.ClientResponseDto
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User

fun Client.toResponseDto() = ClientResponseDto(
    id = id,
    name = name,
    surname = surname,
    phoneNumber = phoneNumber,
    userId = user.id,
    addressId = address.id
)

fun ClientRequestDto.toModel(user: User, direccion: Direccion) = Client(
    id = 0, // al crear es 0
    phoneNumber = phoneNumber,
    name = name,
    surname = surname,
    user = user,
    address = direccion
)
