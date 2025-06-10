package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.PedidoCreateRequestDto
import dev.javi.rugs_903_back.models.Pedido
import dev.javi.rugs_903_back.models.PedidoLinea
import dev.javi.rugs_903_back.repositories.ClientRepository
import dev.javi.rugs_903_back.repositories.CustomProductRepository
import dev.javi.rugs_903_back.repositories.PedidoLineaRepository
import dev.javi.rugs_903_back.repositories.PedidosRepository
import dev.javi.rugs_903_back.repositories.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
class PedidoServiceImpl(
    private val pedidosRepository: PedidosRepository,
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository,
    private val customProductRepository: CustomProductRepository, // ✅ añadimos este
    private val pedidoLineaRepository: PedidoLineaRepository
) : PedidoService {

    override fun findAll(): List<Pedido> = pedidosRepository.findAll()

    override fun findById(id: Long): Pedido? = pedidosRepository.findById(id).orElse(null)

    override fun deleteById(id: Long) = pedidosRepository.deleteById(id)

    override fun findByClienteId(clienteId: Long): List<Pedido> {
        val cliente = clientRepository.findById(clienteId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: $clienteId") }

        return pedidosRepository.findAll().filter { it.client?.id == cliente.id }
    }

    override fun findByUsername(username: String): List<Pedido> {
        val client = clientRepository.findByUserUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró cliente para el usuario: $username")

        return pedidosRepository.findAll().filter { it.client?.id == client.id }
    }

    override fun savePedidoConLineas(dto: PedidoCreateRequestDto): Pedido {
        val cliente = clientRepository.findByUserId(dto.clienteId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado para el usuario con id ${dto.clienteId}") }

        val fecha = LocalDate.now().toString()

        val pedido = Pedido(
            client = cliente,
            fecha = fecha,
            estado = dto.estado
        )

        // Guardamos primero el Pedido vacío (para tener el ID generado)
        val savedPedido = pedidosRepository.save(pedido)

        // Creamos las líneas de pedido
        val lineas = dto.lineas.map { lineaDto ->
            val producto = productRepository.findById(lineaDto.productId)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID ${lineaDto.productId}") }

            PedidoLinea(
                pedido = savedPedido,
                producto = producto,
                cantidad = lineaDto.cantidad,
                precioUnitario = producto.price,
                total = producto.price * lineaDto.cantidad
            )
        }

        // Guardamos las líneas
        pedidoLineaRepository.saveAll(lineas)

        // Si hay productos personalizados, los asociamos al pedido
        dto.customProductIds?.forEach { customProductId ->
            val customProduct = customProductRepository.findById(customProductId)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Producto personalizado no encontrado con ID: $customProductId") }

            val updatedProduct = customProduct.copy(pedido = savedPedido)
            customProductRepository.save(updatedProduct)
        }

        // Volvemos a cargar el pedido con las líneas
        return pedidosRepository.findById(savedPedido.id)
            .orElseThrow { ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al recuperar el pedido guardado") }
    }


}
