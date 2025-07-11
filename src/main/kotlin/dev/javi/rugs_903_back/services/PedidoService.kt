package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.PedidoCreateRequestDto
import dev.javi.rugs_903_back.dto.PedidoResponseDto
import dev.javi.rugs_903_back.models.Pedido

interface PedidoService {
    fun findAll(): List<Pedido>
    fun findById(id: Long): Pedido?
    fun deleteById(id: Long)
    fun findByClienteId(clienteId: Long): List<Pedido>
    fun findByUsername(username: String): List<Pedido>
    fun savePedidoConLineas(dto: PedidoCreateRequestDto): Pedido
}
