package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.PedidoRequestDto
import dev.javi.rugs_903_back.dto.PedidoResponseDto
import dev.javi.rugs_903_back.mappers.toResponse
import dev.javi.rugs_903_back.services.PedidoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/pedidos")
class PedidoController(
    private val pedidoService: PedidoService
) {

    @GetMapping
    fun getAll(): List<PedidoResponseDto> {
        return pedidoService.findAll().map { it.toResponse() }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): PedidoResponseDto {
        return pedidoService.findById(id)?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado")
    }

    @PostMapping
    fun createPedido(@RequestBody dto: PedidoRequestDto): PedidoResponseDto {
        return pedidoService.save(dto).toResponse()
    }

    @DeleteMapping("/{id}")
    fun deletePedido(@PathVariable id: Long) {
        pedidoService.deleteById(id)
    }

    @GetMapping("/cliente/{clienteId}")
    fun getPedidosByCliente(@PathVariable clienteId: Long): List<PedidoResponseDto> {
        return pedidoService.findByClienteId(clienteId).map { it.toResponse() }
    }
}
