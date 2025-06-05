package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.PedidoResponseDto
import dev.javi.rugs_903_back.models.Pedido

fun Pedido.toResponse(): PedidoResponseDto {
    return PedidoResponseDto(
        id = id,
        clienteId = client?.id ?: 0, // Si no hay cliente, 0 como valor de fallback
        clientName = "${client?.name ?: "Desconocido"} ${client?.surname ?: ""}".trim(),
        productId = producto?.id ?: 0,
        productName = producto?.name ?: "Producto no disponible",
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        total = total,
        fecha = fecha
    )
}

fun List<Pedido>.toResponseList(): List<PedidoResponseDto> {
    return this.map { it.toResponse() }
}
