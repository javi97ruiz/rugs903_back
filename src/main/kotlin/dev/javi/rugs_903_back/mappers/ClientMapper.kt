package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User

fun Client.toResponseDto() = ClientResponseDto(
    id = id,
    name = name,
    surname = surname,
    phoneNumber = phoneNumber,
    userId = user.id,
    addressId = address.id,
    isActive = isActive
)


fun Client.toFullResponseDto() = ClientResponseFullDto(
    id = id,
    name = name,
    surname = surname,
    phoneNumber = phoneNumber,
    user = user.toResponseDto(),
    address = address.toResponseDto(),
    isActive = isActive
)


fun ClientRequestDto.toModel(user: User, direccion: Direccion) = Client(
    id = userId, // o id si estás actualizando
    phoneNumber = phoneNumber,
    name = name,
    surname = surname,
    user = user,
    address = direccion
)
