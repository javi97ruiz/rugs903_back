package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.UserRequestDto
import dev.javi.rugs_903_back.dto.UserResponseDto
import dev.javi.rugs_903_back.models.User
import java.time.LocalDateTime

fun User.toResponseDto(): UserResponseDto = UserResponseDto(
    id = this.id,
    username = this.username,
    isActive = this.isActive,
    rol = this.rol
)

fun UserRequestDto.toUser(): User = User(
    username = this.username,
    password = this.password,
    rol = this.rol,
    isActive = true,
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)


fun List<UserRequestDto>.toUserList(): List<User> {
    return this.map { it.toUser() }
}

fun List<User>.toDtoList(): List<UserResponseDto> {
    return this.map { it.toResponseDto() }
}
