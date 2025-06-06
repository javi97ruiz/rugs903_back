package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.CustomProductSimpleDto
import dev.javi.rugs_903_back.dto.PedidoResponseDto
import dev.javi.rugs_903_back.models.Pedido

fun Pedido.toResponse(): PedidoResponseDto {
    val customProductsDtos = this.customProducts?.map {
        CustomProductSimpleDto(
            id = it.id,
            name = it.name,
            height = it.height,
            length = it.length,
            imageUrl = it.imageUrl
        )
    } ?: emptyList()

    return PedidoResponseDto(
        id = id,
        clienteId = client?.id ?: 0L,          // ✅ así accedes bien
        clientName = client?.name + " " + client?.surname,
        productId = producto?.id ?: 0L,        // ✅ así accedes bien
        productName = producto?.name ?: "Producto no disponible",
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        total = total,
        fecha = fecha,
        customProducts = customProductsDtos,
        estado = estado,
        )

}


fun List<Pedido>.toResponseList(): List<PedidoResponseDto> {
    return this.map { it.toResponse() }
}
