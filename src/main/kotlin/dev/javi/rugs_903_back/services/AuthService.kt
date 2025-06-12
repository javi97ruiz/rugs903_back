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
        try {
            // Intentar buscar por username
            var user = userRepository.findByUsername(loginRequest.username)

            // Si no lo encuentra, intentar por email
            if (user == null) {
                user = userRepository.findByEmail(loginRequest.username)
            }

            // Si no existe → 401
            if (user == null) {
                throw UsernameNotFoundException("Usuario no encontrado")
            }

            // Si está inactivo → lanzar excepción
            if (!user.isActive) {
                throw IllegalStateException("Cuenta desactivada")
            }

            // Validar credenciales
            val authToken = UsernamePasswordAuthenticationToken(user.username, loginRequest.password)
            authenticationManager.authenticate(authToken)

            // Generar token
            val token = jwtTokenProvider.generateToken(user.username, user.rol)

            return JwtResponse(
                token = token,
                id = user.id,
                username = user.username,
                rol = user.rol
            )
        } catch (ex: UsernameNotFoundException) {
            println("❌ Error de login: ${ex.message}")
            throw ex // tu controller debería mapear UsernameNotFoundException → 401
        } catch (ex: IllegalStateException) {
            println("❌ Cuenta desactivada: ${ex.message}")
            throw ex // tu controller debería mapear IllegalStateException → 403
        } catch (ex: Exception) {
            println("❌ Error inesperado en login: ${ex.message}")
            throw RuntimeException("Error al intentar autenticarse", ex)
        }
    }


}
