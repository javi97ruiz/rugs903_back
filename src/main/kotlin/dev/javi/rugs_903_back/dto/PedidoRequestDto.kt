package dev.javi.rugs_903_back.dto

data class PedidoRequestDto(
    val clienteId: Long,
    val productId: Long,
    val cantidad: Int
)
