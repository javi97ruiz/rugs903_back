package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class CustomUserDetailsServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var service: CustomUserDetailsService

    private fun buildUser(
        username: String = "testuser",
        password: String = "password",
        rol: String = "USER",
        isActive: Boolean = true
    ): User {
        return User(
            id = 1L,
            username = username,
            email = "$username@test.com",
            password = password,
            rol = rol,
            isActive = isActive,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    @Test
    fun `loadUserByUsername should return UserDetails when user is active`() {
        val user = buildUser()
        `when`(userRepository.findByUsername(user.username)).thenReturn(user)

        val userDetails = service.loadUserByUsername(user.username)

        assertEquals(user.username, userDetails.username)
        assertEquals(user.password, userDetails.password)
        assertTrue(userDetails.authorities.any { it.authority == "ROLE_${user.rol.uppercase()}" })
    }

    @Test
    fun `loadUserByUsername should throw UsernameNotFoundException when user not found`() {
        val username = "unknownuser"
        `when`(userRepository.findByUsername(username)).thenReturn(null)

        assertThrows<UsernameNotFoundException> {
            service.loadUserByUsername(username)
        }
    }

    @Test
    fun `loadUserByUsername should throw DisabledException when user is inactive`() {
        val user = buildUser(isActive = false)
        `when`(userRepository.findByUsername(user.username)).thenReturn(user)

        assertThrows<DisabledException> {
            service.loadUserByUsername(user.username)
        }
    }
}
