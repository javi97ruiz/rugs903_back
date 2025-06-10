// PedidoCreateRequestDto.kt
package dev.javi.rugs_903_back.dto

data class PedidoCreateRequestDto(
    val clienteId: Long,
    val estado: String = "pendiente",
    val lineas: List<PedidoLineaRequestDto>,
    val customProductIds: List<Long>? = emptyList()
)

// PedidoLineaRequestDto.kt
data class PedidoLineaRequestDto(
    val productId: Long,
    val cantidad: Int
)
