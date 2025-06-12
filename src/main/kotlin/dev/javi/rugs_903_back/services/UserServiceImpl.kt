package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.UserUpdateRequest
import dev.javi.rugs_903_back.dto.UserUpdateRequestDto
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.ClientRepository
import dev.javi.rugs_903_back.repositories.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val clientRepository: ClientRepository
): UserService   {


    override fun getAll(active: Boolean?): List<User> {
        return when (active) {
            true -> userRepository.findAllByIsActiveTrue()
            false -> userRepository.findAllByIsActiveFalse()
            null -> userRepository.findAll()
        }
    }


    override fun getById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    override fun create(user: User): User {
        return userRepository.save(user)
    }

    override fun deleteById(id: Long) {
        val user = userRepository.findById(id).orElseThrow { RuntimeException("User not found") }
        user.isActive = false
        userRepository.save(user)

        // También desactivar el Client asociado (si existe)
        val client = clientRepository.findByUserId(user.id)
        if (client != null) {
            client.get().isActive = false
            clientRepository.save(client.get())
        }
    }



    override fun update(id: Long, dto: UserUpdateRequestDto): User? {
        val userExistente = userRepository.findById(id).orElse(null) ?: return null

        if (dto.password != null && dto.password.isNotBlank()) {
            userExistente.password = passwordEncoder.encode(dto.password)
        }

        userExistente.username = dto.username
        userExistente.rol = dto.rol

        return userRepository.save(userExistente)
    }


    override fun getByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    override fun updateByUsername(username: String, dto: UserUpdateRequest): User {
        val existingUser = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Usuario no encontrado")

        val updatedUser = existingUser.copy(
            username = dto.username,
            password = if (dto.password.isNullOrBlank()) existingUser.password
            else passwordEncoder.encode(dto.password),
            updatedAt = LocalDateTime.now()
        )

        return userRepository.save(updatedUser)
    }



}