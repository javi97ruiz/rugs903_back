package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.ProductRequestDto
import dev.javi.rugs_903_back.dto.ProductResponseDto
import dev.javi.rugs_903_back.models.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProductMapperTest {

    @Test
    fun `toResponse should map Product to ProductResponseDto correctly`() {
        val product = Product(
            id = 1L,
            name = "Producto Test",
            description = "Descripcion Test",
            price = 99.99,
            quantity = 10,
            imagen = "https://example.com/image.jpg",
            isActive = true
        )

        val dto = product.toResponse()

        assertEquals(product.id, dto.id)
        assertEquals(product.name, dto.name)
        assertEquals(product.description, dto.description)
        assertEquals(product.price, dto.price)
        assertEquals(product.quantity, dto.quantity)
        assertEquals(product.imagen, dto.imagen)
        assertEquals(product.isActive, dto.isActive)
    }

    @Test
    fun `toModel should map ProductRequestDto to Product correctly`() {
        val requestDto = ProductRequestDto(
            name = "Producto Nuevo",
            description = "Descripcion Nuevo",
            price = 49.99,
            quantity = 5,
            imagen = "https://example.com/new-image.jpg"
        )

        val product = requestDto.toModel()

        // id por defecto es 0
        assertEquals(0L, product.id)
        assertEquals(requestDto.name, product.name)
        assertEquals(requestDto.description, product.description)
        assertEquals(requestDto.price, product.price)
        assertEquals(requestDto.quantity, product.quantity)
        assertEquals(requestDto.imagen, product.imagen)
        assertEquals(true, product.isActive) // valor por defecto en tu model
    }

    @Test
    fun `toResponseList should map List of Product to List of ProductResponseDto`() {
        val products = listOf(
            Product(
                id = 1L,
                name = "Producto 1",
                description = "Descripcion 1",
                price = 10.0,
                quantity = 100,
                imagen = "img1",
                isActive = true
            ),
            Product(
                id = 2L,
                name = "Producto 2",
                description = "Descripcion 2",
                price = 20.0,
                quantity = 200,
                imagen = "img2",
                isActive = false
            )
        )

        val dtos = products.toResponseList()

        assertEquals(products.size, dtos.size)

        products.zip(dtos).forEach { (product, dto) ->
            assertEquals(product.id, dto.id)
            assertEquals(product.name, dto.name)
            assertEquals(product.description, dto.description)
            assertEquals(product.price, dto.price)
            assertEquals(product.quantity, dto.quantity)
            assertEquals(product.imagen, dto.imagen)
            assertEquals(product.isActive, dto.isActive)
        }
    }
}
