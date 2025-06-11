package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.UserUpdateRequest
import dev.javi.rugs_903_back.dto.UserUpdateRequestDto
import dev.javi.rugs_903_back.models.User

interface UserService {
    fun getAll(): List<User>
    fun getById(id: Long): User?
    fun create(user: User): User
    fun update(id: Long, dto: UserUpdateRequestDto): User?
    fun deleteById(id: Long)
    fun getByUsername(username: String): User?
    fun updateByUsername(username: String, dto: UserUpdateRequest): User

}