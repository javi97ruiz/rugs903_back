package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.Client

fun Client.toClientProfileDto() = ClientProfileDto(
    id = id,
    name = name,
    surname = surname,
    phoneNumber = phoneNumber,
    user = UserBasicDto(
        id = user.id,
        username = user.username
    ),
    address = DireccionDto(
        id = address.id,
        calle = address.calle,
        ciudad = address.ciudad,
        codigoPostal = address.codigoPostal,
        provincia = address.provincia,
        numero = address.numero,
        portal = address.portal,
        piso = address.piso
    )
)
