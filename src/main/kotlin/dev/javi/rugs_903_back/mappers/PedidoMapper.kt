// PedidoMapper.kt
package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.Pedido

fun Pedido.toResponse(): PedidoResponseDto {
    val customProductsDtos = this.customProducts?.map {
        CustomProductSimpleDto(
            id = it.id,
            name = it.name,
            height = it.height,
            length = it.length,
            imageUrl = it.imageUrl,
            price = it.price
        )
    } ?: emptyList()

    val lineasDto = this.lineas.map { linea ->
        PedidoLineaResponseDto(
            productId = linea.producto.id,
            productName = linea.producto.name,
            cantidad = linea.cantidad,
            precioUnitario = linea.precioUnitario,
            total = linea.total
        )
    }

    return PedidoResponseDto(
        id = id,
        clienteId = client.id,
        clientName = "${client.name} ${client.surname}",
        lineas = lineasDto,
        fecha = fecha,
        customProducts = customProductsDtos,
        estado = estado,
        totalPedido = lineas.sumOf { it.total }
    )
}

fun List<Pedido>.toResponseList(): List<PedidoResponseDto> {
    return this.map { it.toResponse() }
}
