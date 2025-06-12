package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.services.UserService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
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
import java.security.Principal
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userService: UserService

    private fun sampleUser(id: Long = 1L) = User(
        id = id,
        username = "testuser",
        email = "test@example.com",
        password = "password",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        isActive = true,
        rol = "user"
    )

    @Test
    fun `getAllUsers should return list of users`() {
        val users = listOf(sampleUser(), sampleUser(id = 2L))
        whenever(userService.getAll(null)).thenReturn(users)

        mockMvc.perform(
            get("/users/admin")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].username").value("testuser"))
    }

    @Test
    fun `getMyProfile should return current user profile`() {
        val user = sampleUser()
        whenever(userService.getByUsername("user")).thenReturn(user)

        mockMvc.perform(
            get("/users/me")
                .with(user("user").roles("USER")) // esto ya inyecta el principal con name = "user"
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id))
            .andExpect(jsonPath("$.username").value(user.username))
            .andExpect(jsonPath("$.rol").value(user.rol))
            .andExpect(jsonPath("$.isActive").value(user.isActive))
    }


    @Test
    fun `updateCurrentUser should update and return updated user`() {
        val updateRequest = UserUpdateRequest(username = "newuser", password = "newpass")
        val updatedUser = sampleUser().copy(username = "newuser")

        whenever(userService.updateByUsername(eq("testuser"), eq(updateRequest))).thenReturn(updatedUser)

        mockMvc.perform(
            put("/users/me")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("newuser"))
    }

    @Test
    fun `getUserById should return user`() {
        val user = sampleUser()
        whenever(userService.getById(1L)).thenReturn(user)

        mockMvc.perform(
            get("/users/1")
                .with(user("user").roles("USER"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testuser"))
    }

    @Test
    fun `createUser should create and return new user`() {
        val userRequestDto = UserRequestDto(username = "newuser", email = "new@example.com", password = "password", rol = "user")
        val createdUser = sampleUser(id = 10L).copy(username = "newuser", email = "new@example.com")

        whenever(userService.create(any())).thenReturn(createdUser)

        mockMvc.perform(
            post("/users")
                .with(user("admin").roles("ADMIN")) // normalmente creación es admin
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.rol").value("user"))
    }

    @Test
    fun `updateUser should update and return updated user`() {
        val updateRequestDto = UserUpdateRequestDto(username = "updatedUser", password = "newpass", rol = "admin")
        val updatedUser = sampleUser().copy(username = "updatedUser", rol = "admin")

        whenever(userService.update(eq(1L), eq(updateRequestDto))).thenReturn(updatedUser)

        mockMvc.perform(
            put("/users/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("updatedUser"))
            .andExpect(jsonPath("$.rol").value("admin"))
    }

    @Test
    fun `deleteUser should call service deleteById`() {
        doNothing().whenever(userService).deleteById(1L)


        mockMvc.perform(
            delete("/users/1")
                .with(user("admin").roles("ADMIN"))
        )
            .andExpect(status().isOk)

        verify(userService).deleteById(1L)
    }
}
