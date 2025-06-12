package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.DireccionDto
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.services.DireccionService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.Mockito.*
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DireccionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var direccionService: DireccionService

    // --------- /direcciones ------------

    @Test
    fun `getAll should return 200`() {
        `when`(direccionService.getAll()).thenReturn(listOf(buildDireccion()))

        mockMvc.perform(
            get("/direcciones")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].calle").value("Calle Falsa"))
    }

    @Test
    fun `getById should return 200 when found`() {
        `when`(direccionService.getById(1L)).thenReturn(buildDireccion())

        mockMvc.perform(
            get("/direcciones/1")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.calle").value("Calle Falsa"))
    }

    @Test
    fun `getById should return 404 when not found`() {
        `when`(direccionService.getById(999L)).thenReturn(null)

        mockMvc.perform(
            get("/direcciones/999")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `createDireccion should return 200 when successful`() {
        val request = buildDireccionDto()

        `when`(direccionService.save(any())).thenReturn(buildDireccion())

        mockMvc.perform(
            post("/direcciones")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.calle").value("Calle Falsa"))
    }

    @Test
    fun `updateDireccion should return 200 when successful`() {
        val request = buildDireccionDto()

        `when`(direccionService.update(any())).thenReturn(buildDireccion())

        mockMvc.perform(
            put("/direcciones/1")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.calle").value("Calle Falsa"))
    }

    @Test
    fun `updateDireccion should return 400 when path id and body id do not match`() {
        val request = buildDireccionDto().copy(id = 999L)

        mockMvc.perform(
            put("/direcciones/1")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `updateDireccion should return 404 when not found`() {
        val request = buildDireccionDto()

        `when`(direccionService.update(any())).thenReturn(null)

        mockMvc.perform(
            put("/direcciones/1")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deleteDireccion should return 200 when successful`() {
        `when`(direccionService.getById(1L)).thenReturn(buildDireccion())
        doNothing().`when`(direccionService).deleteById(1L)

        mockMvc.perform(
            delete("/direcciones/1")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `deleteDireccion should return 404 when not found`() {
        `when`(direccionService.getById(999L)).thenReturn(null)

        mockMvc.perform(
            delete("/direcciones/999")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isNotFound)
    }

    // --------- Helpers ------------

    private fun buildDireccion(): Direccion {
        return Direccion(
            id = 1L,
            calle = "Calle Falsa",
            numero = "123",
            portal = "A",
            piso = "1",
            codigoPostal = "28080",
            ciudad = "Madrid",
            provincia = "Madrid"
        )
    }

    private fun buildDireccionDto(): DireccionDto {
        return DireccionDto(
            id = 1L,
            calle = "Calle Falsa",
            numero = "123",
            portal = "A",
            piso = "1",
            codigoPostal = "28080",
            ciudad = "Madrid",
            provincia = "Madrid"
        )
    }
}
