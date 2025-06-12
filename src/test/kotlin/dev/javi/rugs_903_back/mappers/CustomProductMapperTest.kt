package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.CustomProductRequestDto
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.CustomProduct
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.Pedido
import dev.javi.rugs_903_back.models.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CustomProductMapperTest {

    @Test
    fun `toResponseDto should map CustomProduct to CustomProductResponseDto correctly`() {
        val client = Client(
            id = 100L,
            phoneNumber = "612345678",
            name = "Cliente Test",
            surname = "Apellidos Test",
            user = User(
                id = 200L,
                username = "userTest",
                email = "user@test.com",
                password = "password",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                isActive = true,
                rol = "USER"
            ),
            address = Direccion(
                id = 300L,
                calle = "Calle Prueba",
                numero = "1",
                portal = "A",
                piso = "1",
                codigoPostal = "28001",
                ciudad = "Madrid",
                provincia = "Madrid"
            ),
            isActive = true
        )

        val pedido = Pedido(
            id = 10L,
            client = client,
            fecha = "2025-06-12",
            estado = "pendiente",
            lineas = mutableListOf(),
            customProducts = emptyList()
        )

        val customProduct = CustomProduct(
            id = 1L,
            name = "Alfombra personalizada",
            height = 200,
            length = 300,
            imageUrl = "https://example.com/image.jpg",
            price = 99.99,
            pedido = pedido
        )

        val dto = customProduct.toResponseDto()

        assertEquals(customProduct.id, dto.id)
        assertEquals(customProduct.name, dto.name)
        assertEquals(customProduct.height, dto.height)
        assertEquals(customProduct.length, dto.length)
        assertEquals(customProduct.imageUrl, dto.imageUrl)
        assertEquals(customProduct.price, dto.price)
        assertEquals(customProduct.pedido?.id, dto.pedidoId)
    }


    @Test
    fun `toModel should map CustomProductRequestDto to CustomProduct correctly`() {
        val requestDto = CustomProductRequestDto(
            name = "Alfombra personalizada pequeña",
            height = 150,
            length = 200,
            imageUrl = "https://example.com/image2.jpg",
            price = 59.99
        )

        val customProduct = requestDto.toModel()

        assertEquals(0L, customProduct.id) // porque no se pasa id en toModel(), es autogenerado
        assertEquals(requestDto.name, customProduct.name)
        assertEquals(requestDto.height, customProduct.height)
        assertEquals(requestDto.length, customProduct.length)
        assertEquals(requestDto.imageUrl, customProduct.imageUrl)
        assertEquals(requestDto.price, customProduct.price)
        assertEquals(null, customProduct.pedido) // siempre null en toModel()
    }
}
