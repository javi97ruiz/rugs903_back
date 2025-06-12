package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.DireccionDto
import dev.javi.rugs_903_back.models.Direccion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DireccionMapperTest {

    @Test
    fun `toResponseDto should map Direccion to DireccionDto correctly`() {
        val direccion = Direccion(
            id = 1L,
            calle = "Calle Falsa",
            numero = "123",
            portal = "A",
            piso = "1",
            codigoPostal = "28080",
            ciudad = "Madrid",
            provincia = "Madrid"
        )

        val dto = direccion.toResponseDto()

        assertEquals(direccion.id, dto.id)
        assertEquals(direccion.calle, dto.calle)
        assertEquals(direccion.numero, dto.numero)
        assertEquals(direccion.portal, dto.portal)
        assertEquals(direccion.piso, dto.piso)
        assertEquals(direccion.codigoPostal, dto.codigoPostal)
        assertEquals(direccion.ciudad, dto.ciudad)
        assertEquals(direccion.provincia, dto.provincia)
    }

    @Test
    fun `toModel should map DireccionDto to Direccion correctly`() {
        val dto = DireccionDto(
            id = 2L,
            calle = "Avenida Siempre Viva",
            numero = "742",
            portal = "B",
            piso = "2",
            codigoPostal = "12345",
            ciudad = "Springfield",
            provincia = "Illinois"
        )

        val direccion = dto.toModel()

        assertEquals(dto.id, direccion.id)
        assertEquals(dto.calle, direccion.calle)
        assertEquals(dto.numero, direccion.numero)
        assertEquals(dto.portal, direccion.portal)
        assertEquals(dto.piso, direccion.piso)
        assertEquals(dto.codigoPostal, direccion.codigoPostal)
        assertEquals(dto.ciudad, direccion.ciudad)
        assertEquals(dto.provincia, direccion.provincia)
    }

    @Test
    fun `toDtoList should map List of Direccion to List of DireccionDto correctly`() {
        val direcciones = listOf(
            Direccion(
                id = 1L,
                calle = "Calle Uno",
                numero = "1",
                portal = "A",
                piso = "1",
                codigoPostal = "11111",
                ciudad = "Ciudad1",
                provincia = "Provincia1"
            ),
            Direccion(
                id = 2L,
                calle = "Calle Dos",
                numero = "2",
                portal = "B",
                piso = "2",
                codigoPostal = "22222",
                ciudad = "Ciudad2",
                provincia = "Provincia2"
            )
        )

        val dtos = direcciones.toDtoList()

        assertEquals(direcciones.size, dtos.size)

        direcciones.zip(dtos).forEach { (direccion, dto) ->
            assertEquals(direccion.id, dto.id)
            assertEquals(direccion.calle, dto.calle)
            assertEquals(direccion.numero, dto.numero)
            assertEquals(direccion.portal, dto.portal)
            assertEquals(direccion.piso, dto.piso)
            assertEquals(direccion.codigoPostal, dto.codigoPostal)
            assertEquals(direccion.ciudad, dto.ciudad)
            assertEquals(direccion.provincia, dto.provincia)
        }
    }

    @Test
    fun `toDireccionList should map List of DireccionDto to List of Direccion correctly`() {
        val dtos = listOf(
            DireccionDto(
                id = 1L,
                calle = "Calle Uno",
                numero = "1",
                portal = "A",
                piso = "1",
                codigoPostal = "11111",
                ciudad = "Ciudad1",
                provincia = "Provincia1"
            ),
            DireccionDto(
                id = 2L,
                calle = "Calle Dos",
                numero = "2",
                portal = "B",
                piso = "2",
                codigoPostal = "22222",
                ciudad = "Ciudad2",
                provincia = "Provincia2"
            )
        )

        val direcciones = dtos.toDireccionList()

        assertEquals(dtos.size, direcciones.size)

        dtos.zip(direcciones).forEach { (dto, direccion) ->
            assertEquals(dto.id, direccion.id)
            assertEquals(dto.calle, direccion.calle)
            assertEquals(dto.numero, direccion.numero)
            assertEquals(dto.portal, direccion.portal)
            assertEquals(dto.piso, direccion.piso)
            assertEquals(dto.codigoPostal, direccion.codigoPostal)
            assertEquals(dto.ciudad, direccion.ciudad)
            assertEquals(dto.provincia, direccion.provincia)
        }
    }
}
