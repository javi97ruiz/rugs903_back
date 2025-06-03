package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.PedidoRequestDto
import dev.javi.rugs_903_back.dto.PedidoResponseDto
import dev.javi.rugs_903_back.models.Pedido

fun Pedido.toResponse(): PedidoResponseDto {
    return PedidoResponseDto(
        id = id,
        clienteId = clienteId,
        clientName = client?.name + " " + client?.surname,
        productId = productId,
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
