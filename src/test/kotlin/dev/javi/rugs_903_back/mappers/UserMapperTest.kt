package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.UserRequestDto
import dev.javi.rugs_903_back.dto.UserResponseDto
import dev.javi.rugs_903_back.models.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserMapperTest {

    @Test
    fun `toResponseDto should map User to UserResponseDto correctly`() {
        val user = User(
            id = 1L,
            username = "userTest",
            email = "user@test.com",
            password = "password",
            rol = "ADMIN",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val dto = user.toResponseDto()

        assertEquals(user.id, dto.id)
        assertEquals(user.username, dto.username)
        assertEquals(user.rol, dto.rol)
        assertEquals(user.isActive, dto.isActive)
    }

    @Test
    fun `toUser should map UserRequestDto to User correctly`() {
        val requestDto = UserRequestDto(
            username = "newuser",
            email = "newuser@test.com",
            password = "newpass",
            rol = "USER"
        )

        val user = requestDto.toUser()

        assertEquals(0L, user.id) // por defecto
        assertEquals(requestDto.username, user.username)
        assertEquals(requestDto.email, user.email)
        assertEquals(requestDto.password, user.password)
        assertEquals(requestDto.rol, user.rol)
        assertTrue(user.isActive)
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
    }

    @Test
    fun `toUserList should map List of UserRequestDto to List of User`() {
        val requests = listOf(
            UserRequestDto(
                username = "user1",
                email = "user1@test.com",
                password = "pass1",
                rol = "USER"
            ),
            UserRequestDto(
                username = "user2",
                email = "user2@test.com",
                password = "pass2",
                rol = "ADMIN"
            )
        )

        val users = requests.toUserList()

        assertEquals(requests.size, users.size)

        requests.zip(users).forEach { (request, user) ->
            assertEquals(0L, user.id)
            assertEquals(request.username, user.username)
            assertEquals(request.email, user.email)
            assertEquals(request.password, user.password)
            assertEquals(request.rol, user.rol)
            assertTrue(user.isActive)
            assertNotNull(user.createdAt)
            assertNotNull(user.updatedAt)
        }
    }

    @Test
    fun `toDtoList should map List of User to List of UserResponseDto`() {
        val users = listOf(
            User(
                id = 1L,
                username = "user1",
                email = "user1@test.com",
                password = "pass1",
                rol = "USER",
                isActive = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            User(
                id = 2L,
                username = "user2",
                email = "user2@test.com",
                password = "pass2",
                rol = "ADMIN",
                isActive = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        val dtos = users.toDtoList()

        assertEquals(users.size, dtos.size)

        users.zip(dtos).forEach { (user, dto) ->
            assertEquals(user.id, dto.id)
            assertEquals(user.username, dto.username)
            assertEquals(user.rol, dto.rol)
            assertEquals(user.isActive, dto.isActive)
        }
    }
}
