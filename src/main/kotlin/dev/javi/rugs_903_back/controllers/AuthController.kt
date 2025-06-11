package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.LoginRequest
import dev.javi.rugs_903_back.dto.JwtResponse
import dev.javi.rugs_903_back.dto.RegisterRequest
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.DireccionRepository
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.security.JwtTokenProvider
import dev.javi.rugs_903_back.services.AuthService
import dev.javi.rugs_903_back.services.ClientService
import jakarta.transaction.Transactional
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
    private val passwordEncoder: PasswordEncoder,
    private val clientService: ClientService,
    private val direccionRepository: DireccionRepository
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        return try {
            val response = authService.login(loginRequest)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas o error interno: ${e.message}")
        }
    }

    @PostMapping("/register")
    @Transactional
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Any> {
        if (userRepository.findByUsername(registerRequest.username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe")
        }

        // 1️⃣ Crear User
        val user = User(
            username = registerRequest.username,
            password = passwordEncoder.encode(registerRequest.password),
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            rol = registerRequest.rol
        )
        val savedUser = userRepository.save(user)

        // 2️⃣ Crear Direccion
        val direccion = Direccion(
            calle = registerRequest.address.calle,
            numero = registerRequest.address.numero,
            portal = registerRequest.address.portal,
            piso = registerRequest.address.piso,
            codigoPostal = registerRequest.address.codigoPostal,
            ciudad = registerRequest.address.ciudad,
            provincia = registerRequest.address.provincia
        )
        val savedDireccion = direccionRepository.save(direccion)

        // 3️⃣ Crear Client
        val client = Client(
            address = savedDireccion,
            phoneNumber = registerRequest.phoneNumber,
            name = registerRequest.name,
            surname = registerRequest.surname,
            user = savedUser
        )
        clientService.save(client)

        return ResponseEntity.ok("Usuario registrado con éxito")
    }

    @GetMapping("/me")
    fun getCurrentUser(principal: Principal): ResponseEntity<JwtResponse> {
        val user = userRepository.findByUsername(principal.name)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val response = JwtResponse(
            token = "",
            id = user.id,
            username = user.username,
            rol = user.rol
        )
        return ResponseEntity.ok(response)
    }
}
