package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ClientProfileMapperTest {

    @Test
    fun `toClientProfileDto should map Client to ClientProfileDto correctly`() {
        val user = User(
            id = 1L,
            username = "testuser",
            email = "test@example.com",
            password = "pass",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isActive = true,
            rol = "USER"
        )

        val address = Direccion(
            id = 2L,
            calle = "Calle Falsa",
            numero = "123",
            portal = "A",
            piso = "1",
            codigoPostal = "28080",
            ciudad = "Madrid",
            provincia = "Madrid"
        )

        val client = Client(
            id = 3L,
            phoneNumber = "612345678",
            name = "John",
            surname = "Doe",
            user = user,
            address = address,
            isActive = true
        )

        val profileDto = client.toClientProfileDto()

        // Pruebo campos de ClientProfileDto
        assertEquals(client.id, profileDto.id)
        assertEquals(client.name, profileDto.name)
        assertEquals(client.surname, profileDto.surname)
        assertEquals(client.phoneNumber, profileDto.phoneNumber)

        // Pruebo campos de UserBasicDto
        assertEquals(client.user.id, profileDto.user.id)
        assertEquals(client.user.username, profileDto.user.username)

        // Pruebo campos de DireccionDto
        assertEquals(client.address.id, profileDto.address.id)
        assertEquals(client.address.calle, profileDto.address.calle)
        assertEquals(client.address.numero, profileDto.address.numero)
        assertEquals(client.address.portal, profileDto.address.portal)
        assertEquals(client.address.piso, profileDto.address.piso)
        assertEquals(client.address.codigoPostal, profileDto.address.codigoPostal)
        assertEquals(client.address.ciudad, profileDto.address.ciudad)
        assertEquals(client.address.provincia, profileDto.address.provincia)
    }
}
