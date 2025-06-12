package dev.javi.rugs_903_back.mappers

import dev.javi.rugs_903_back.dto.*
import dev.javi.rugs_903_back.models.CustomProduct
import dev.javi.rugs_903_back.models.Pedido
import dev.javi.rugs_903_back.models.PedidoLinea

fun Pedido.toResponse(): PedidoResponseDto {
    val totalProductos = this.lineas.sumOf { it.total }
    val totalCustomProducts = this.customProducts.sumOf { it.price }
    val totalPedido = totalProductos + totalCustomProducts

    return PedidoResponseDto(
        id = this.id,
        clienteId = this.client.id,
        clientName = "${this.client.name} ${this.client.surname}",
        lineas = this.lineas.map { it.toResponse() },
        fecha = this.fecha,
        customProducts = this.customProducts.map { it.toSimpleDto() },
        estado = this.estado,
        totalPedido = totalPedido
    )
}

fun List<Pedido>.toResponseList(): List<PedidoResponseDto> = this.map { it.toResponse() }

// Mappers auxiliares:

fun PedidoLinea.toResponse(): PedidoLineaResponseDto =
    PedidoLineaResponseDto(
        productId = this.producto.id,
        productName = this.producto.name,
        cantidad = this.cantidad,
        precioUnitario = this.precioUnitario,
        total = this.total,
        productImage = this.producto.imagen
    )

fun CustomProduct.toSimpleDto(): CustomProductSimpleDto =
    CustomProductSimpleDto(
        id = this.id,
        name = this.name,
        height = this.height,
        length = this.length,
        imageUrl = this.imageUrl,
        price = this.price
    )
