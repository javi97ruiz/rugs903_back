package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.PedidoCreateRequestDto
import dev.javi.rugs_903_back.dto.PedidoLineaRequestDto
import dev.javi.rugs_903_back.models.*
import dev.javi.rugs_903_back.repositories.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class PedidoServiceImplTest {

    @Mock lateinit var pedidosRepository: PedidosRepository
    @Mock lateinit var clientRepository: ClientRepository
    @Mock lateinit var productRepository: ProductRepository
    @Mock lateinit var customProductRepository: CustomProductRepository
    @Mock lateinit var pedidoLineaRepository: PedidoLineaRepository

    @InjectMocks
    lateinit var pedidoService: PedidoServiceImpl

    private fun buildClient(id: Long = 1L): Client {
        return Client(
            id = id,
            phoneNumber = "612345678",
            name = "John",
            surname = "Doe",
            user = User(
                id = 1L,
                username = "johndoe",
                email = "john@example.com",
                password = "password",
                rol = "USER",
                isActive = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            address = Direccion(
                id = 1L,
                calle = "Calle Falsa",
                numero = "123",
                portal = "A",
                piso = "1",
                codigoPostal = "28080",
                ciudad = "Madrid",
                provincia = "Madrid"
            ),
            isActive = true
        )
    }

    private fun buildProduct(id: Long = 1L, price: Double = 10.0): Product {
        return Product(
            id = id,
            name = "Product $id",
            description = "Desc $id",
            price = price,
            quantity = 10,
            imagen = "img$id",
            isActive = true
        )
    }

    private fun buildCustomProduct(id: Long = 1L): CustomProduct {
        return CustomProduct(
            id = id,
            name = "Custom $id",
            height = 100,
            length = 200,
            imageUrl = "img$id",
            price = 20.0,
            pedido = null
        )
    }

    private fun buildPedido(id: Long = 1L, client: Client): Pedido {
        return Pedido(
            id = id,
            client = client,
            fecha = LocalDate.now().toString(),
            estado = "Pendiente",
            lineas = mutableListOf(),
            customProducts = emptyList()
        )
    }

    @Test
    fun `findAll should return list of Pedido`() {
        val pedidos = listOf(buildPedido(1L, buildClient()), buildPedido(2L, buildClient()))
        `when`(pedidosRepository.findAll()).thenReturn(pedidos)

        val result = pedidoService.findAll()

        assertEquals(pedidos, result)
    }

    @Test
    fun `findById should return Pedido when found`() {
        val pedido = buildPedido(1L, buildClient())
        `when`(pedidosRepository.findById(1L)).thenReturn(Optional.of(pedido))

        val result = pedidoService.findById(1L)

        assertEquals(pedido, result)
    }

    @Test
    fun `findById should return null when not found`() {
        `when`(pedidosRepository.findById(1L)).thenReturn(Optional.empty())

        val result = pedidoService.findById(1L)

        assertNull(result)
    }

    @Test
    fun `deleteById should call repository deleteById`() {
        pedidoService.deleteById(1L)

        verify(pedidosRepository).deleteById(1L)
    }

    @Test
    fun `findByClienteId should return pedidos when client exists`() {
        val client = buildClient(1L)
        val pedidos = listOf(buildPedido(1L, client), buildPedido(2L, client))

        `when`(clientRepository.findById(client.id)).thenReturn(Optional.of(client))
        `when`(pedidosRepository.findAll()).thenReturn(pedidos)

        val result = pedidoService.findByClienteId(client.id)

        assertEquals(pedidos, result)
    }

    @Test
    fun `findByClienteId should throw ResponseStatusException when client not found`() {
        `when`(clientRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<ResponseStatusException> {
            pedidoService.findByClienteId(1L)
        }
    }

    @Test
    fun `findByUsername should return pedidos when client exists`() {
        val client = buildClient(1L)
        val pedidos = listOf(buildPedido(1L, client), buildPedido(2L, client))

        `when`(clientRepository.findByUserUsername(client.user.username)).thenReturn(client)
        `when`(pedidosRepository.findAll()).thenReturn(pedidos)

        val result = pedidoService.findByUsername(client.user.username)

        assertEquals(pedidos, result)
    }

    @Test
    fun `findByUsername should throw ResponseStatusException when client not found`() {
        `when`(clientRepository.findByUserUsername("unknown")).thenReturn(null)

        assertThrows<ResponseStatusException> {
            pedidoService.findByUsername("unknown")
        }
    }

    @Test
    fun `savePedidoConLineas should save Pedido with lines and custom products`() {
        val client = buildClient(1L)
        val pedido = buildPedido(1L, client)
        val product = buildProduct(1L)
        val customProduct = buildCustomProduct(1L)

        val dto = PedidoCreateRequestDto(
            clienteId = client.user.id,
            estado = "Pendiente",
            lineas = listOf(PedidoLineaRequestDto(productId = product.id, cantidad = 2)),
            customProductIds = listOf(customProduct.id)
        )

        `when`(clientRepository.findByUserId(client.user.id)).thenReturn(Optional.of(client))
        `when`(pedidosRepository.save(any())).thenReturn(pedido)
        `when`(productRepository.findById(product.id)).thenReturn(Optional.of(product))
        `when`(customProductRepository.findById(customProduct.id)).thenReturn(Optional.of(customProduct))
        `when`(pedidosRepository.findById(pedido.id)).thenReturn(Optional.of(pedido))

        val result = pedidoService.savePedidoConLineas(dto)

        assertEquals(pedido, result)

        verify(pedidoLineaRepository).saveAll(anyList())
        verify(customProductRepository, times(1)).save(any())
    }

    @Test
    fun `savePedidoConLineas should throw ResponseStatusException when client not found`() {
        val dto = PedidoCreateRequestDto(
            clienteId = 1L,
            estado = "Pendiente",
            lineas = emptyList(),
            customProductIds = emptyList()
        )

        `when`(clientRepository.findByUserId(dto.clienteId)).thenReturn(Optional.empty())

        assertThrows<ResponseStatusException> {
            pedidoService.savePedidoConLineas(dto)
        }
    }

    @Test
    fun `savePedidoConLineas should throw ResponseStatusException when product not found`() {
        val client = buildClient(1L)

        val dto = PedidoCreateRequestDto(
            clienteId = client.user.id,
            estado = "Pendiente",
            lineas = listOf(PedidoLineaRequestDto(productId = 999L, cantidad = 1)),
            customProductIds = emptyList()
        )

        `when`(clientRepository.findByUserId(client.user.id)).thenReturn(Optional.of(client))
        `when`(pedidosRepository.save(any())).thenReturn(buildPedido(1L, client))
        `when`(productRepository.findById(999L)).thenReturn(Optional.empty())

        assertThrows<ResponseStatusException> {
            pedidoService.savePedidoConLineas(dto)
        }
    }

    @Test
    fun `savePedidoConLineas should throw ResponseStatusException when custom product not found`() {
        val client = buildClient(1L)
        val product = buildProduct(1L)

        val dto = PedidoCreateRequestDto(
            clienteId = client.user.id,
            estado = "Pendiente",
            lineas = listOf(PedidoLineaRequestDto(productId = product.id, cantidad = 1)),
            customProductIds = listOf(999L)
        )

        `when`(clientRepository.findByUserId(client.user.id)).thenReturn(Optional.of(client))
        `when`(pedidosRepository.save(any())).thenReturn(buildPedido(1L, client))
        `when`(productRepository.findById(product.id)).thenReturn(Optional.of(product))
        `when`(customProductRepository.findById(999L)).thenReturn(Optional.empty())

        assertThrows<ResponseStatusException> {
            pedidoService.savePedidoConLineas(dto)
        }
    }
}
