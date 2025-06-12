package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ClientMapperTest {

    @Test
    fun `toResponseDto should map Client to ClientResponseDto correctly`() {
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

        val dto = client.toResponseDto()

        assertEquals(client.id, dto.id)
        assertEquals(client.name, dto.name)
        assertEquals(client.surname, dto.surname)
        assertEquals(client.phoneNumber, dto.phoneNumber)
        assertEquals(client.user.id, dto.userId)
        assertEquals(client.address.id, dto.addressId)
        assertEquals(client.isActive, dto.isActive)
    }

    @Test
    fun `toFullResponseDto should map Client to ClientResponseFullDto correctly`() {
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

        val fullDto = client.toFullResponseDto()

        assertEquals(client.id, fullDto.id)
        assertEquals(client.name, fullDto.name)
        assertEquals(client.surname, fullDto.surname)
        assertEquals(client.phoneNumber, fullDto.phoneNumber)

        // UserResponseDto
        assertEquals(client.user.id, fullDto.user.id)
        assertEquals(client.user.username, fullDto.user.username)
        assertEquals(client.user.rol, fullDto.user.rol)
        assertEquals(client.user.isActive, fullDto.user.isActive)

        // DireccionDto
        assertEquals(client.address.id, fullDto.address.id)
        assertEquals(client.address.calle, fullDto.address.calle)
        assertEquals(client.address.numero, fullDto.address.numero)
        assertEquals(client.address.portal, fullDto.address.portal)
        assertEquals(client.address.piso, fullDto.address.piso)
        assertEquals(client.address.codigoPostal, fullDto.address.codigoPostal)
        assertEquals(client.address.ciudad, fullDto.address.ciudad)
        assertEquals(client.address.provincia, fullDto.address.provincia)

        assertEquals(client.isActive, fullDto.isActive)
    }

    @Test
    fun `toModel should map ClientRequestDto to Client correctly`() {
        val requestDto = ClientRequestDto(
            userId = 1L,
            addressId = 2L,
            phoneNumber = "698765432",
            name = "Jane",
            surname = "Smith"
        )

        val user = User(
            id = requestDto.userId,
            username = "janesmith",
            email = "jane@example.com",
            password = "pass",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isActive = true,
            rol = "USER"
        )

        val address = Direccion(
            id = requestDto.addressId,
            calle = "Otra Calle",
            numero = "456",
            portal = "B",
            piso = "2",
            codigoPostal = "12345",
            ciudad = "Barcelona",
            provincia = "Barcelona"
        )

        val client = requestDto.toModel(user, address)

        // NOTA: en tu mapper, id = userId --> esto es un poco raro, lo respetamos
        assertEquals(requestDto.userId, client.id)
        assertEquals(requestDto.name, client.name)
        assertEquals(requestDto.surname, client.surname)
        assertEquals(requestDto.phoneNumber, client.phoneNumber)
        assertEquals(user, client.user)
        assertEquals(address, client.address)
    }
}
