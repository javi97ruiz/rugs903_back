package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.ProductRequestDto
import dev.javi.rugs_903_back.models.Product
import dev.javi.rugs_903_back.services.ProductService
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
class ProductControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var productService: ProductService

    // --------- /products ------------

    @Test
    fun `getAll should return 200`() {
        `when`(productService.getAll(null)).thenReturn(listOf(buildProduct()))

        mockMvc.perform(
            get("/products")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Producto de prueba"))
    }

    @Test
    fun `getById should return 200 when found`() {
        `when`(productService.getById(1L)).thenReturn(buildProduct())

        mockMvc.perform(
            get("/products/1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Producto de prueba"))
    }

    @Test
    fun `getById should return 404 when not found`() {
        `when`(productService.getById(999L)).thenReturn(null)

        mockMvc.perform(
            get("/products/999")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `create should return 200 when successful`() {
        val request = buildProductRequestDto()

        `when`(productService.create(any())).thenReturn(buildProduct())

        mockMvc.perform(
            post("/products")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Producto de prueba"))
    }

    @Test
    fun `update should return 200 when successful`() {
        val request = buildProductRequestDto()

        `when`(productService.update(eq(1L), any())).thenReturn(buildProduct())

        mockMvc.perform(
            put("/products/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Producto de prueba"))
    }

    @Test
    fun `update should return 404 when not found`() {
        val request = buildProductRequestDto()

        `when`(productService.update(eq(999L), any())).thenReturn(null)

        mockMvc.perform(
            put("/products/999")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `delete should return 200 when successful`() {
        `when`(productService.getById(1L)).thenReturn(buildProduct())
        doNothing().`when`(productService).deleteById(1L)

        mockMvc.perform(
            delete("/products/1")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `delete should return 404 when not found`() {
        `when`(productService.getById(999L)).thenReturn(null)

        mockMvc.perform(
            delete("/products/999")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isNotFound)
    }

    // --------- Helpers ------------

    private fun buildProduct(): Product {
        return Product(
            id = 1L,
            name = "Producto de prueba",
            description = "Descripción de prueba",
            price = 25.99,
            quantity = 10,
            isActive = true,
            imagen = "https://example.com/image.jpg"
        )
    }

    private fun buildProductRequestDto(): ProductRequestDto {
        return ProductRequestDto(
            name = "Producto de prueba",
            description = "Descripción de prueba",
            price = 25.99,
            quantity = 10,
            imagen = "https://example.com/image.jpg"
        )
    }
}
