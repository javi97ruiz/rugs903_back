package dev.javi.rugs_903_back.dto

data class PedidoResponseDto(
    val id: Long,
    val clienteId: Long,
    val clientName: String,
    val productId: Long,
    val productName: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val total: Double,
    val fecha: String
)
