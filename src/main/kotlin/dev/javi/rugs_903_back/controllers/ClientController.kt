package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.ClientRequestDto
import dev.javi.rugs_903_back.dto.ClientResponseDto
import dev.javi.rugs_903_back.dto.ClientResponseFullDto
import dev.javi.rugs_903_back.mappers.toFullResponseDto
import dev.javi.rugs_903_back.mappers.toModel
import dev.javi.rugs_903_back.mappers.toResponseDto
import dev.javi.rugs_903_back.repositories.DireccionRepository
import dev.javi.rugs_903_back.repositories.UserRepository
import dev.javi.rugs_903_back.services.ClientService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@RestController
@RequestMapping("/clients")
class ClientController(
    private val clientService: ClientService,
    private val userRepository: UserRepository,
    private val direccionRepository: DireccionRepository
) {

    @GetMapping
    fun getAllClients(): List<ClientResponseDto> {
        return clientService.getAll().map { it.toResponseDto() }
    }

    @GetMapping("/{id}")
    fun getClientById(@PathVariable id: Long): ClientResponseDto {
        return clientService.getById(id)?.toResponseDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado")
    }

    @PostMapping
    fun createClient(@RequestBody dto: ClientRequestDto): ClientResponseDto {
        val user = userRepository.findById(dto.userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")
        }

        val direccion = direccionRepository.findById(dto.addressId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada")
        }

        val client = dto.toModel(user, direccion)
        return clientService.save(client).toResponseDto()
    }

    @PutMapping("/{id}")
    fun updateClient(@PathVariable id: Long, @RequestBody dto: ClientRequestDto): ClientResponseDto {
        val user = userRepository.findById(dto.userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")
        }

        val direccion = direccionRepository.findById(dto.addressId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada")
        }

        val client = dto.toModel(user, direccion).copy(id = id)
        return clientService.save(client).toResponseDto()
    }

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Long) {
        clientService.deleteById(id)
    }

    // Nuevo endpoint para obtener los datos completos del cliente autenticado
    @GetMapping("/me")
    fun getCurrentClient(principal: Principal): ClientResponseFullDto {
        val client = clientService.getByUsername(principal.name)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado")
        return client.toFullResponseDto()
    }

    @GetMapping("/admin")
    fun getAllClientsFull(): List<ClientResponseFullDto> {
        return clientService.getAll().map { it.toFullResponseDto() }
    }


}
