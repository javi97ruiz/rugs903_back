package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.CustomProductSimpleDto
import dev.javi.rugs_903_back.dto.PedidoLineaResponseDto
import dev.javi.rugs_903_back.dto.PedidoResponseDto
import dev.javi.rugs_903_back.models.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PedidoMapperTest {

    @Test
    fun `toResponse should map Pedido to PedidoResponseDto correctly`() {
        val client = Client(
            id = 100L,
            phoneNumber = "612345678",
            name = "John",
            surname = "Doe",
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

        val product1 = Product(
            id = 1L,
            name = "Producto 1",
            description = "Desc 1",
            price = 10.0,
            quantity = 100,
            isActive = true,
            imagen = "img1"
        )

        val product2 = Product(
            id = 2L,
            name = "Producto 2",
            description = "Desc 2",
            price = 20.0,
            quantity = 200,
            isActive = true,
            imagen = "img2"
        )

        val customProduct1 = CustomProduct(
            id = 1L,
            name = "Custom 1",
            height = 100,
            length = 200,
            imageUrl = "imgCustom1",
            price = 30.0,
            pedido = null
        )

        val customProduct2 = CustomProduct(
            id = 2L,
            name = "Custom 2",
            height = 150,
            length = 250,
            imageUrl = "imgCustom2",
            price = 40.0,
            pedido = null
        )

        val pedido = Pedido(
            id = 10L,
            client = client,
            fecha = "2025-06-12",
            estado = "pendiente",
            lineas = mutableListOf(), // primero vacío
            customProducts = listOf(customProduct1, customProduct2)
        )

        val linea1 = PedidoLinea(
            id = 1L,
            pedido = pedido, // ahora sí le pasas el Pedido real
            producto = product1,
            cantidad = 2,
            precioUnitario = 10.0,
            total = 20.0
        )

        val linea2 = PedidoLinea(
            id = 2L,
            pedido = pedido, // ahora sí le pasas el Pedido real
            producto = product2,
            cantidad = 1,
            precioUnitario = 20.0,
            total = 20.0
        )

        // luego añades las líneas al pedido:
        pedido.lineas.addAll(listOf(linea1, linea2))

        val dto = pedido.toResponse()

        assertEquals(pedido.id, dto.id)
        assertEquals(pedido.client.id, dto.clienteId)
        assertEquals("${pedido.client.name} ${pedido.client.surname}", dto.clientName)
        assertEquals(pedido.fecha, dto.fecha)
        assertEquals(pedido.estado, dto.estado)

        // totalPedido = sum(lineas.total) + sum(customProducts.price)
        val expectedTotalPedido = pedido.lineas.sumOf { it.total } + pedido.customProducts.sumOf { it.price }
        assertEquals(expectedTotalPedido, dto.totalPedido)

        // test de lineas
        assertEquals(pedido.lineas.size, dto.lineas.size)
        pedido.lineas.zip(dto.lineas).forEach { (linea, lineaDto) ->
            assertEquals(linea.producto.id, lineaDto.productId)
            assertEquals(linea.producto.name, lineaDto.productName)
            assertEquals(linea.cantidad, lineaDto.cantidad)
            assertEquals(linea.precioUnitario, lineaDto.precioUnitario)
            assertEquals(linea.total, lineaDto.total)
        }

        // test de customProducts
        assertEquals(pedido.customProducts.size, dto.customProducts.size)
        pedido.customProducts.zip(dto.customProducts).forEach { (custom, customDto) ->
            assertEquals(custom.id, customDto.id)
            assertEquals(custom.name, customDto.name)
            assertEquals(custom.height, customDto.height)
            assertEquals(custom.length, customDto.length)
            assertEquals(custom.imageUrl, customDto.imageUrl)
            assertEquals(custom.price, customDto.price)
        }
    }

    @Test
    fun `toResponseList should map List of Pedido to List of PedidoResponseDto`() {
        val pedido1 = Pedido(
            id = 1L,
            client = Client(
                id = 1L,
                phoneNumber = "600000000",
                name = "Client1",
                surname = "Surname1",
                user = User(
                    id = 1L,
                    username = "user1",
                    email = "user1@test.com",
                    password = "password",
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    isActive = true,
                    rol = "USER"
                ),
                address = Direccion(
                    id = 1L,
                    calle = "Calle 1",
                    numero = "1",
                    portal = "A",
                    piso = "1",
                    codigoPostal = "11111",
                    ciudad = "Ciudad1",
                    provincia = "Provincia1"
                ),
                isActive = true
            ),
            fecha = "2025-06-12",
            estado = "pendiente",
            lineas = mutableListOf(),
            customProducts = emptyList()
        )

        val pedido2 = Pedido(
            id = 2L,
            client = pedido1.client,
            fecha = "2025-06-13",
            estado = "enviado",
            lineas = mutableListOf(),
            customProducts = emptyList()
        )

        val pedidos = listOf(pedido1, pedido2)

        val dtos = pedidos.toResponseList()

        assertEquals(pedidos.size, dtos.size)
        pedidos.zip(dtos).forEach { (pedido, dto) ->
            assertEquals(pedido.id, dto.id)
            assertEquals(pedido.client.id, dto.clienteId)
            assertEquals("${pedido.client.name} ${pedido.client.surname}", dto.clientName)
            assertEquals(pedido.fecha, dto.fecha)
            assertEquals(pedido.estado, dto.estado)
        }
    }
}
