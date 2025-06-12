package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.PedidoCreateRequestDto
import dev.javi.rugs_903_back.dto.PedidoLineaRequestDto
import dev.javi.rugs_903_back.models.*
import dev.javi.rugs_903_back.services.PedidoService
import dev.javi.rugs_903_back.repositories.PedidosRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var pedidoService: PedidoService

    @MockBean
    lateinit var pedidosRepository: PedidosRepository

    // --------- /pedidos/admin ------------

    @Test
    fun `getAll should return 200`() {
        `when`(pedidoService.findAll()).thenReturn(listOf(buildPedido()))

        mockMvc.perform(
            get("/pedidos/admin")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].estado").value("pendiente"))
    }

    @Test
    fun `getById should return 200 when found`() {
        `when`(pedidoService.findById(1L)).thenReturn(buildPedido())

        mockMvc.perform(
            get("/pedidos/1")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.estado").value("pendiente"))
    }

    @Test
    fun `getById should return 404 when not found`() {
        `when`(pedidoService.findById(999L)).thenReturn(null)

        mockMvc.perform(
            get("/pedidos/999")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `createPedido should return 200 when successful`() {
        val request = buildPedidoCreateRequestDto()

        `when`(pedidoService.savePedidoConLineas(any())).thenReturn(buildPedido())

        mockMvc.perform(
            post("/pedidos")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.estado").value("pendiente"))
    }

    @Test
    fun `deletePedido should return 200`() {
        doNothing().`when`(pedidoService).deleteById(1L)

        mockMvc.perform(
            delete("/pedidos/1")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `getPedidosByCliente should return 200`() {
        `when`(pedidoService.findByClienteId(1L)).thenReturn(listOf(buildPedido()))

        mockMvc.perform(
            get("/pedidos/cliente/1")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].estado").value("pendiente"))
    }

    // --------- /pedidos/me ------------

    @Test
    fun `getMyPedidos should return 200`() {
        `when`(pedidoService.findByUsername("user")).thenReturn(listOf(buildPedido()))

        mockMvc.perform(
            get("/pedidos/me")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].estado").value("pendiente"))
    }

    // --------- /pedidos/{id}/cancelar ------------

    @Test
    fun `cancelarPedido should return 200 when successful`() {
        val pedido = buildPedido()

        `when`(pedidoService.findById(1L)).thenReturn(pedido)
        `when`(pedidosRepository.save(any())).thenReturn(pedido.copy(estado = "cancelado"))

        mockMvc.perform(
            put("/pedidos/1/cancelar")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.estado").value("cancelado"))
    }

    @Test
    fun `cancelarPedido should return 404 when not found`() {
        `when`(pedidoService.findById(999L)).thenReturn(null)

        mockMvc.perform(
            put("/pedidos/999/cancelar")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `cancelarPedido should return 400 when already cancelado`() {
        val pedido = buildPedido().copy(estado = "cancelado")

        `when`(pedidoService.findById(1L)).thenReturn(pedido)

        mockMvc.perform(
            put("/pedidos/1/cancelar")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `cancelarPedido should return 400 when already enviado`() {
        val pedido = buildPedido().copy(estado = "enviado")

        `when`(pedidoService.findById(1L)).thenReturn(pedido)

        mockMvc.perform(
            put("/pedidos/1/cancelar")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isBadRequest)
    }

    // --------- Helpers ------------

    private fun buildPedido(): Pedido {
        return Pedido(
            id = 1L,
            client = buildClient(),
            fecha = "2025-06-12",
            estado = "pendiente",
            lineas = mutableListOf(buildPedidoLinea()),
            customProducts = listOf(buildCustomProduct())
        )
    }

    private fun buildPedidoCreateRequestDto(): PedidoCreateRequestDto {
        return PedidoCreateRequestDto(
            clienteId = 1L,
            estado = "Pendiente",
            lineas = listOf(
                PedidoLineaRequestDto(
                    productId = 1L,
                    cantidad = 2
                )
            ),
            customProductIds = listOf(1L)
        )
    }

    private fun buildPedidoLinea(): PedidoLinea {
        return PedidoLinea(
            id = 1L,
            pedido = mock(Pedido::class.java), // para evitar ciclo infinito
            producto = buildProduct(),
            cantidad = 2,
            precioUnitario = 50.0,
            total = 100.0
        )
    }

    private fun buildProduct(): Product {
        return Product(
            id = 1L,
            name = "Alfombra",
            description = "Alfombra de prueba",
            price = 50.0,
            quantity = 10,
            isActive = true,
            imagen = "https://example.com/image.jpg"
        )
    }

    private fun buildCustomProduct(): CustomProduct {
        return CustomProduct(
            id = 1L,
            name = "Alfombra personalizada",
            height = 100,
            length = 200,
            imageUrl = "https://example.com/custom.jpg",
            price = 75.0,
            pedido = null
        )
    }

    private fun buildClient(): Client {
        return Client(
            id = 1L,
            address = mock(Direccion::class.java),
            phoneNumber = "612345678",
            name = "John",
            surname = "Doe",
            user = mock(User::class.java)
        )
    }
}
