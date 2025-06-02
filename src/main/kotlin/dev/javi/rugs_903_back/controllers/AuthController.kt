package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.LoginRequest
import dev.javi.rugs_903_back.dto.JwtResponse
import dev.javi.rugs_903_back.dto.RegisterRequest
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.security.JwtTokenProvider
import dev.javi.rugs_903_back.services.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<JwtResponse> {
        val response = authService.login(loginRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Any> {
        if (userRepository.findByUsername(registerRequest.username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe")
        }

        val user = User(
            username = registerRequest.username,
            password = passwordEncoder.encode(registerRequest.password),
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            rol = registerRequest.rol
        )

        userRepository.save(user)
        return ResponseEntity.ok("Usuario registrado con éxito")
    }

    @GetMapping("/me")
    fun getCurrentUser(principal: Principal): ResponseEntity<JwtResponse> {
        val user = userRepository.findByUsername(principal.name)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val response = JwtResponse(
            token = "", // Puedes omitirlo o dejarlo vacío
            id = user.id,
            username = user.username,
            rol = user.rol
        )
        return ResponseEntity.ok(response)
    }



}
