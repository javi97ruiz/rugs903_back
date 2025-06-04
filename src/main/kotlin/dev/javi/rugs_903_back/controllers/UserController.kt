package dev.javi.rugs_903_back.controllers
import dev.javi.rugs_903_back.dto.UserRequestDto
import dev.javi.rugs_903_back.dto.UserResponseDto
import dev.javi.rugs_903_back.dto.UserUpdateRequest
import dev.javi.rugs_903_back.mappers.toResponseDto
import dev.javi.rugs_903_back.mappers.toUser
import dev.javi.rugs_903_back.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/admin")
    fun getAllUsers(): List<UserResponseDto> =
        userService.getAll().map { it.toResponseDto() }

    @GetMapping("/me")
    fun getMyProfile(principal: Principal): UserResponseDto {
        val user = userService.getByUsername(principal.name)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")
        return user.toResponseDto()
    }

    @PutMapping("/me")
    fun updateCurrentUser(
        @RequestBody dto: UserUpdateRequest,
        authentication: Authentication
    ): UserResponseDto {
        val currentUsername = authentication.name
        val user = userService.updateByUsername(currentUsername, dto)
        return user.toResponseDto()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UserResponseDto =
        userService.getById(id)?.toResponseDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping
    fun createUser(@RequestBody dto: UserRequestDto): UserResponseDto =
        userService.create(dto.toUser()).toResponseDto()

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody dto: UserRequestDto): UserResponseDto =
        userService.update(id, dto.toUser())?.toResponseDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteById(id)
    }
}

