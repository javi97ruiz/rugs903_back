// PedidoResponseDto.kt
package dev.javi.rugs_903_back.dto

data class PedidoResponseDto(
    val id: Long,
    val clienteId: Long,
    val clientName: String,
    val lineas: List<PedidoLineaResponseDto>,
    val fecha: String,
    val customProducts: List<CustomProductSimpleDto> = emptyList(),
    val estado: String,
    val totalPedido: Double
)

data class PedidoLineaResponseDto(
    val productId: Long,
    val productName: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val total: Double
)


