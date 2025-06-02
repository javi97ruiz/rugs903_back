package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.PedidoRequestDto
import dev.javi.rugs_903_back.models.Pedido

interface PedidoService {
    fun findAll(): List<Pedido>
    fun findById(id: Long): Pedido?
    fun save(dto: PedidoRequestDto): Pedido
    fun deleteById(id: Long)
    fun findByClienteId(clienteId: Long): List<Pedido>
}
