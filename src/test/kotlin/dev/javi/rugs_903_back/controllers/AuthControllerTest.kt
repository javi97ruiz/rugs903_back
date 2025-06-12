package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.exceptions.UserException
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.DireccionRepository
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.security.JwtTokenProvider
import dev.javi.rugs_903_back.services.AuthService
import dev.javi.rugs_903_back.services.ClientService
import dev.javi.rugs_903_back.services.CustomUserDetailsService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.security.Principal
import java.time.LocalDateTime
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

    @MockBean
    lateinit var customUserDetailsService: CustomUserDetailsService

    @MockBean
    lateinit var authService: AuthService

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var passwordEncoder: PasswordEncoder

    @MockBean
    lateinit var clientService: ClientService

    @MockBean
    lateinit var direccionRepository: DireccionRepository

    // --------- /auth/login ------------

    @Test
    fun `login should return 200 when successful`() {
        val loginRequest = LoginRequest(username = "user", password = "pass00")
        val jwtResponse = JwtResponse(token = "token", id = 1L, username = "user", rol = "USER")

        `when`(authService.login(loginRequest)).thenReturn(jwtResponse)

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value("token"))
            .andExpect(jsonPath("$.username").value("user"))
    }

    @Test
    fun `login should return 401 when UsernameNotFoundException`() {
        val loginRequest = LoginRequest(username = "wrong", password = "pass00")

        `when`(authService.login(loginRequest)).thenThrow(UserException.UsernameNotFoundException("Not found"))

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `login should return 403 when IllegalStateException`() {
        val loginRequest = LoginRequest(username = "inactive", password = "pass00")

        `when`(authService.login(loginRequest)).thenThrow(IllegalStateException("Cuenta desactivada"))

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `login should return 401 when generic exception`() {
        val loginRequest = LoginRequest(username = "error", password = "pass00")

        `when`(authService.login(loginRequest)).thenThrow(RuntimeException("Other error"))

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isUnauthorized)
    }

    // --------- /auth/register ------------

    @Test
    fun `register should return 400 when username already exists`() {
        val registerRequest = buildRegisterRequest()

        `when`(userRepository.findByUsername(registerRequest.username)).thenReturn(User(
            id = 1L,
            username = registerRequest.username,
            email = registerRequest.email,
            password = "pass",
            rol = "USER",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ))

        mockMvc.perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `register should return 400 when email already exists`() {
        val registerRequest = buildRegisterRequest()

        `when`(userRepository.findByUsername(registerRequest.username)).thenReturn(null)
        `when`(userRepository.findByEmail(registerRequest.email)).thenReturn(User(
            id = 1L,
            username = registerRequest.username,
            email = registerRequest.email,
            password = "pass",
            rol = "USER",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ))

        mockMvc.perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `register should return 200 when successful`() {
        val registerRequest = buildRegisterRequest()

        `when`(userRepository.findByUsername(registerRequest.username)).thenReturn(null)
        `when`(userRepository.findByEmail(registerRequest.email)).thenReturn(null)
        `when`(passwordEncoder.encode(registerRequest.password)).thenReturn("encodedPass")
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }
        `when`(direccionRepository.save(any())).thenAnswer { it.arguments[0] }

        mockMvc.perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isOk)
    }

    // --------- /auth/me ------------

    @Test
    fun `getCurrentUser should return 200 when user exists`() {
        val user = User(
            id = 1L,
            username = "user",
            email = "user@test.com",
            password = "pass",
            rol = "USER",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(userRepository.findByUsername(user.username)).thenReturn(user)

        mockMvc.perform(
            get("/auth/me")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("user"))
    }


    @Test
    fun `getCurrentUser should return 404 when user not found`() {
        `when`(userRepository.findByUsername("unknown")).thenReturn(null)

        val principal = Principal { "unknown" }

        mockMvc.perform(
            get("/auth/me").with(user("unknown"))
        )
            .andExpect(status().isNotFound)
    }

    // --------- Helper ---------

    private fun buildRegisterRequest(): RegisterRequest {
        return RegisterRequest(
            email = "test@test.com",
            username = "testuser",
            password = "password123",
            rol = "USER",
            name = "Test",
            surname = "User",
            phoneNumber = "612345678",
            address = AddressDtoForRegister(
                calle = "Calle Falsa",
                numero = "123",
                portal = "A",
                piso = "1",
                codigoPostal = "28080",
                ciudad = "Madrid",
                provincia = "Madrid"
            )
        )
    }
}
