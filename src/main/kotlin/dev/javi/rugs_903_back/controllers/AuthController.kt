package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.LoginRequest
import dev.javi.rugs_903_back.dto.JwtResponse
import dev.javi.rugs_903_back.dto.RegisterRequest
import dev.javi.rugs_903_back.exceptions.UserException
import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.DireccionRepository
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.security.JwtTokenProvider
import dev.javi.rugs_903_back.services.AuthService
import dev.javi.rugs_903_back.services.ClientService
import jakarta.transaction.Transactional
import jakarta.validation.Valid
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
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        return try {
            val response = authService.login(loginRequest)
            ResponseEntity.ok(response)
        } catch (ex: UserException.UsernameNotFoundException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas")
        } catch (ex: IllegalStateException) {
            // Si la cuenta está desactivada
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cuenta desactivada. Si cree que es un error, póngase en contacto")
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas")
        }
    }


    @PostMapping("/register")
    @Transactional
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<Any> {

        // Verificar username duplicado
        if (userRepository.findByUsername(registerRequest.username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario ya está en uso")
        }

        // Verificar email duplicado
        if (userRepository.findByEmail(registerRequest.email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya está en uso")
        }

        // 1️⃣ Crear User
        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
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
