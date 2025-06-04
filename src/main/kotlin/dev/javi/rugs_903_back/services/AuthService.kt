package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.JwtResponse
import dev.javi.rugs_903_back.dto.LoginRequest
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun login(loginRequest: LoginRequest): JwtResponse {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw UsernameNotFoundException("Usuario no encontrado")

        // Validamos credenciales usando el AuthenticationManager
        val authToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        authenticationManager.authenticate(authToken)

        // Si es correcto, generamos el token
        val token = jwtTokenProvider.generateToken(user.username, user.rol)

        return JwtResponse(
            token = token,
            id = user.id,
            username = user.username,
            rol = user.rol
        )
    }
}
