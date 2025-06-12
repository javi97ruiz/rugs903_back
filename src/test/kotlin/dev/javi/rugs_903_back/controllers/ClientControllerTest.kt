package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.services.ClientService
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.repositories.DireccionRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.kotlin.any
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import java.security.Principal
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var clientService: ClientService

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var direccionRepository: DireccionRepository

    // --------- /clients ------------

    @Test
    fun `getAllClients should return 200`() {
        `when`(clientService.getAll(null)).thenReturn(listOf(buildClient()))

        mockMvc.perform(
            get("/clients")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Test"))
    }

    @Test
    fun `getClientById should return 200 when found`() {
        `when`(clientService.getById(1L)).thenReturn(buildClient())

        mockMvc.perform(
            get("/clients/1")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test"))
    }

    @Test
    fun `getClientById should return 404 when not found`() {
        `when`(clientService.getById(999L)).thenReturn(null)

        mockMvc.perform(
            get("/clients/999")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `createClient should return 200 when successful`() {
        val request = buildClientRequestDto()

        `when`(userRepository.findById(request.userId)).thenReturn(Optional.of(buildUser()))
        `when`(direccionRepository.findById(request.addressId)).thenReturn(Optional.of(buildDireccion()))
        `when`(clientService.save(any())).thenReturn(buildClient())

        mockMvc.perform(
            post("/clients")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test"))
    }

    @Test
    fun `createClient should return 404 when user not found`() {
        val request = buildClientRequestDto()

        `when`(userRepository.findById(request.userId)).thenReturn(Optional.empty())

        mockMvc.perform(
            post("/clients")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `createClient should return 404 when direccion not found`() {
        val request = buildClientRequestDto()

        `when`(userRepository.findById(request.userId)).thenReturn(Optional.of(buildUser()))
        `when`(direccionRepository.findById(request.addressId)).thenReturn(Optional.empty())

        mockMvc.perform(
            post("/clients")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `updateClient should return 200 when successful`() {
        val request = buildClientRequestDto()

        `when`(userRepository.findById(request.userId)).thenReturn(Optional.of(buildUser()))
        `when`(direccionRepository.findById(request.addressId)).thenReturn(Optional.of(buildDireccion()))
        `when`(clientService.save(any())).thenReturn(buildClient())

        mockMvc.perform(
            put("/clients/1")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test"))
    }

    @Test
    fun `updateClient should return 404 when user not found`() {
        val request = buildClientRequestDto()

        `when`(userRepository.findById(request.userId)).thenReturn(Optional.empty())

        mockMvc.perform(
            put("/clients/1")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `updateClient should return 404 when direccion not found`() {
        val request = buildClientRequestDto()

        `when`(userRepository.findById(request.userId)).thenReturn(Optional.of(buildUser()))
        `when`(direccionRepository.findById(request.addressId)).thenReturn(Optional.empty())

        mockMvc.perform(
            put("/clients/1")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deleteClient should return 200`() {
        doNothing().`when`(clientService).deleteById(1L)

        mockMvc.perform(
            delete("/clients/1")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
    }

    // --------- /clients/me ------------

    @Test
    fun `getCurrentClient should return 200 when found`() {
        `when`(clientService.getByUsername("user")).thenReturn(buildClient())

        mockMvc.perform(
            get("/clients/me")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test"))
    }

    @Test
    fun `getCurrentClient should return 404 when not found`() {
        `when`(clientService.getByUsername("user")).thenReturn(null)

        mockMvc.perform(
            get("/clients/me")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isNotFound)
    }

    // --------- /clients/admin ------------

    @Test
    fun `getAllClientsFull should return 200`() {
        `when`(clientService.getAll(null)).thenReturn(listOf(buildClient()))

        mockMvc.perform(
            get("/clients/admin")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Test"))
    }

    // --------- Helpers ------------

    private fun buildClient(): Client {
        return Client(
            id = 1L,
            address = buildDireccion(),
            phoneNumber = "612345678",
            name = "Test",
            surname = "User",
            user = buildUser()
        )
    }

    private fun buildClientRequestDto(): ClientRequestDto {
        return ClientRequestDto(
            phoneNumber = "612345678",
            name = "Test",
            surname = "User",
            addressId = 1L,
            userId = 1L
        )
    }

    private fun buildUser(): User {
        return User(
            id = 1L,
            username = "user",
            email = "user@test.com",
            password = "password",
            rol = "USER",
            isActive = true,
            createdAt = java.time.LocalDateTime.now(),
            updatedAt = java.time.LocalDateTime.now()
        )
    }

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
}
