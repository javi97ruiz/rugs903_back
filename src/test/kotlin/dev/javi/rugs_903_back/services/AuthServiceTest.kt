package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.JwtResponse
import dev.javi.rugs_903_back.dto.LoginRequest
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.security.JwtTokenProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var authenticationManager: AuthenticationManager

    @Mock
    lateinit var jwtTokenProvider: JwtTokenProvider

    @InjectMocks
    lateinit var authService: AuthService

    @Test
    fun `login should authenticate user by username and return JwtResponse`() {
        val username = "testuser"
        val password = "password"
        val user = User(
            id = 1L,
            username = username,
            email = "test@test.com",
            password = password,
            rol = "USER",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val token = "mocked.jwt.token"

        `when`(userRepository.findByUsername(username)).thenReturn(user)
        `when`(jwtTokenProvider.generateToken(user.username, user.rol)).thenReturn(token)

        val loginRequest = LoginRequest(username = username, password = password)

        val response = authService.login(loginRequest)

        // Verificaciones
        verify(authenticationManager).authenticate(
            UsernamePasswordAuthenticationToken(user.username, password)
        )
        verify(jwtTokenProvider).generateToken(user.username, user.rol)

        assertEquals(token, response.token)
        assertEquals(user.id, response.id)
        assertEquals(user.username, response.username)
        assertEquals(user.rol, response.rol)
    }

    @Test
    fun `login should authenticate user by email and return JwtResponse`() {
        val username = "email@test.com"
        val password = "password"
        val user = User(
            id = 2L,
            username = "realusername",
            email = username,
            password = password,
            rol = "ADMIN",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val token = "mocked.jwt.token.email"

        `when`(userRepository.findByUsername(username)).thenReturn(null)
        `when`(userRepository.findByEmail(username)).thenReturn(user)
        `when`(jwtTokenProvider.generateToken(user.username, user.rol)).thenReturn(token)

        val loginRequest = LoginRequest(username = username, password = password)

        val response = authService.login(loginRequest)

        verify(authenticationManager).authenticate(
            UsernamePasswordAuthenticationToken(user.username, password)
        )
        verify(jwtTokenProvider).generateToken(user.username, user.rol)

        assertEquals(token, response.token)
        assertEquals(user.id, response.id)
        assertEquals(user.username, response.username)
        assertEquals(user.rol, response.rol)
    }

    @Test
    fun `login should throw UsernameNotFoundException if user not found`() {
        val username = "unknownuser"
        val password = "password"

        `when`(userRepository.findByUsername(username)).thenReturn(null)
        `when`(userRepository.findByEmail(username)).thenReturn(null)

        val loginRequest = LoginRequest(username = username, password = password)

        assertThrows<UsernameNotFoundException> {
            authService.login(loginRequest)
        }

        verify(authenticationManager, never()).authenticate(any())
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString())
    }

    @Test
    fun `login should throw IllegalStateException if user is inactive`() {
        val username = "inactiveuser"
        val password = "password"
        val user = User(
            id = 3L,
            username = username,
            email = "inactive@test.com",
            password = password,
            rol = "USER",
            isActive = false, // 👈 INACTIVO
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(userRepository.findByUsername(username)).thenReturn(user)

        val loginRequest = LoginRequest(username = username, password = password)

        assertThrows<IllegalStateException> {
            authService.login(loginRequest)
        }

        verify(authenticationManager, never()).authenticate(any())
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString())
    }
}
