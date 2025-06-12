package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.CustomProductRequestDto
import dev.javi.rugs_903_back.models.CustomProduct
import dev.javi.rugs_903_back.services.CustomProductService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.mockito.kotlin.any
import org.mockito.Mockito.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CustomProductControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var customProductService: CustomProductService

    // --------- /custom-products ------------

    @Test
    fun `getAll should return 200`() {
        `when`(customProductService.findAll()).thenReturn(listOf(buildCustomProduct()))

        mockMvc.perform(
            get("/custom-products")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Custom Rug"))
    }

    @Test
    fun `getById should return 200 when found`() {
        `when`(customProductService.findById(1L)).thenReturn(buildCustomProduct())

        mockMvc.perform(
            get("/custom-products/1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Custom Rug"))
    }

    @Test
    fun `getById should return 404 when not found`() {
        `when`(customProductService.findById(999L)).thenReturn(null)

        mockMvc.perform(
            get("/custom-products/999")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `create should return 200 when successful`() {
        val request = buildCustomProductRequestDto()

        `when`(customProductService.save(any())).thenReturn(buildCustomProduct())

        mockMvc.perform(
            post("/custom-products")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Custom Rug"))
    }

    @Test
    fun `update should return 200 when successful`() {
        val request = buildCustomProductRequestDto()

        `when`(customProductService.update(eq(1L), any())).thenReturn(buildCustomProduct())

        mockMvc.perform(
            put("/custom-products/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Custom Rug"))
    }

    @Test
    fun `update should return 404 when not found`() {
        val request = buildCustomProductRequestDto()

        `when`(customProductService.update(eq(999L), any())).thenReturn(null)

        mockMvc.perform(
            put("/custom-products/999")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `delete should return 204`() {
        doNothing().`when`(customProductService).deleteById(1L)

        mockMvc.perform(
            delete("/custom-products/1")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isNoContent)
    }

    // --------- Helpers ------------

    private fun buildCustomProduct(): CustomProduct {
        return CustomProduct(
            id = 1L,
            name = "Custom Rug",
            height = 100,
            length = 200,
            imageUrl = "https://example.com/rug.jpg",
            price = 99.99,
            pedido = null
        )
    }

    private fun buildCustomProductRequestDto(): CustomProductRequestDto {
        return CustomProductRequestDto(
            name = "Custom Rug",
            height = 100,
            length = 200,
            imageUrl = "https://example.com/rug.jpg",
            price = 99.99
        )
    }
}
