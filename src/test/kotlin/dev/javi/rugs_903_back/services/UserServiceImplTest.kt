package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.UserUpdateRequest
import dev.javi.rugs_903_back.dto.UserUpdateRequestDto
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
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    lateinit var userService: UserServiceImpl

    private fun buildUser(id: Long = 1L, isActive: Boolean = true): User {
        return User(
            id = id,
            username = "user$id",
            email = "user$id@test.com",
            password = "encodedPassword",
            rol = "USER",
            isActive = isActive,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    @Test
    fun `getAll should return active users when active is true`() {
        val users = listOf(buildUser(1L), buildUser(2L))
        `when`(userRepository.findAllByIsActiveTrue()).thenReturn(users)

        val result = userService.getAll(true)

        assertEquals(users, result)
    }

    @Test
    fun `getAll should return inactive users when active is false`() {
        val users = listOf(buildUser(1L, false), buildUser(2L, false))
        `when`(userRepository.findAllByIsActiveFalse()).thenReturn(users)

        val result = userService.getAll(false)

        assertEquals(users, result)
    }

    @Test
    fun `getAll should return all users when active is null`() {
        val users = listOf(buildUser(1L), buildUser(2L))
        `when`(userRepository.findAll()).thenReturn(users)

        val result = userService.getAll(null)

        assertEquals(users, result)
    }

    @Test
    fun `getById should return User when found`() {
        val user = buildUser(1L)
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        val result = userService.getById(1L)

        assertEquals(user, result)
    }

    @Test
    fun `getById should return null when not found`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val result = userService.getById(1L)

        assertNull(result)
    }

    @Test
    fun `create should save and return User`() {
        val user = buildUser(1L)
        `when`(userRepository.save(user)).thenReturn(user)

        val result = userService.create(user)

        assertEquals(user, result)
    }

    @Test
    fun `deleteById should deactivate User when found`() {
        val user = buildUser(1L)
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        userService.deleteById(1L)

        assertFalse(user.isActive)
        verify(userRepository).save(user)
    }

    @Test
    fun `deleteById should throw RuntimeException when User not found`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<RuntimeException> {
            userService.deleteById(1L)
        }

        verify(userRepository, never()).save(any())
    }

    @Test
    fun `update should update fields and encode password when provided`() {
        val user = buildUser(1L)
        val dto = UserUpdateRequestDto(username = "newUsername", rol = "ADMIN", password = "newPass")

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(passwordEncoder.encode(dto.password)).thenReturn("encodedNewPass")
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = userService.update(1L, dto)

        assertNotNull(result)
        assertEquals(dto.username, result?.username)
        assertEquals(dto.rol, result?.rol)
        assertEquals("encodedNewPass", result?.password)
    }

    @Test
    fun `update should not change password when password is null`() {
        val user = buildUser(1L)
        val dto = UserUpdateRequestDto(username = "newUsername", rol = "ADMIN", password = null)

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = userService.update(1L, dto)

        assertNotNull(result)
        assertEquals(dto.username, result?.username)
        assertEquals(dto.rol, result?.rol)
        assertEquals("encodedPassword", result?.password) // original password unchanged

        verify(passwordEncoder, never()).encode(any())
    }

    @Test
    fun `update should return null when User not found`() {
        val dto = UserUpdateRequestDto(username = "newUsername", rol = "ADMIN", password = "newPass")

        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val result = userService.update(1L, dto)

        assertNull(result)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `getByUsername should return User when found`() {
        val user = buildUser(1L)
        `when`(userRepository.findByUsername(user.username)).thenReturn(user)

        val result = userService.getByUsername(user.username)

        assertEquals(user, result)
    }

    @Test
    fun `getByUsername should return null when not found`() {
        `when`(userRepository.findByUsername("unknown")).thenReturn(null)

        val result = userService.getByUsername("unknown")

        assertNull(result)
    }

    @Test
    fun `updateByUsername should update fields and encode password when provided`() {
        val user = buildUser(1L)
        val dto = UserUpdateRequest(username = "newUsername", password = "newPass")

        `when`(userRepository.findByUsername(user.username)).thenReturn(user)
        `when`(passwordEncoder.encode(dto.password)).thenReturn("encodedNewPass")
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = userService.updateByUsername(user.username, dto)

        assertNotNull(result)
        assertEquals(dto.username, result.username)
        assertEquals("encodedNewPass", result.password)
    }

    @Test
    fun `updateByUsername should not change password when password is blank`() {
        val user = buildUser(1L)
        val dto = UserUpdateRequest(username = "newUsername", password = " ")

        `when`(userRepository.findByUsername(user.username)).thenReturn(user)
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = userService.updateByUsername(user.username, dto)

        assertNotNull(result)
        assertEquals(dto.username, result.username)
        assertEquals("encodedPassword", result.password) // original password unchanged

        verify(passwordEncoder, never()).encode(any())
    }

    @Test
    fun `updateByUsername should throw UsernameNotFoundException when User not found`() {
        val dto = UserUpdateRequest(username = "newUsername", password = "newPass")

        `when`(userRepository.findByUsername("unknown")).thenReturn(null)

        assertThrows<UsernameNotFoundException> {
            userService.updateByUsername("unknown", dto)
        }

        verify(userRepository, never()).save(any())
    }
}
