package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.DireccionDto
import dev.javi.rugs_903_back.models.Direccion

fun Direccion.toResponseDto(): DireccionDto{
    return DireccionDto(
        id = this.id,
        calle = this.calle,
        numero = this.numero,
        portal = this.portal,
        piso = this.piso,
        codigoPostal = this.codigoPostal,
        ciudad = this.ciudad,
        provincia = this.provincia
    )
}

fun DireccionDto.toModel(): Direccion{
    return Direccion(
        id = this.id,
        calle = this.calle,
        numero = this.numero,
        portal = this.portal,
        piso = this.piso,
        codigoPostal = this.codigoPostal,
        ciudad = this.ciudad,
        provincia = this.provincia
    )
}

fun List<Direccion>.toDtoList(): List<DireccionDto> {
    return this.map { it.toResponseDto() }
}

fun List<DireccionDto>.toDireccionList(): List<Direccion> {
    return this.map { it.toModel() }
}