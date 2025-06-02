package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.PedidoRequestDto
import dev.javi.rugs_903_back.models.Pedido
import dev.javi.rugs_903_back.repositories.ClientRepository
import dev.javi.rugs_903_back.repositories.PedidosRepository
import dev.javi.rugs_903_back.repositories.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PedidoServiceImpl(
    private val pedidosRepository: PedidosRepository,
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository
) : PedidoService {

    override fun findAll(): List<Pedido> = pedidosRepository.findAll()

    override fun findById(id: Long): Pedido? = pedidosRepository.findById(id).orElse(null)

    override fun save(dto: PedidoRequestDto): Pedido {
        val cliente = clientRepository.findById(dto.clienteId)
            .orElseThrow { RuntimeException("Cliente no encontrado") }

        val producto = productRepository.findById(dto.productId)
            .orElseThrow { RuntimeException("Producto no encontrado") }

        val precioUnitario = producto.price
        val total = precioUnitario * dto.cantidad
        val fecha = LocalDate.now().toString()

        val pedido = Pedido(
            clienteId = dto.clienteId,
            productId = dto.productId,
            cantidad = dto.cantidad,
            precioUnitario = precioUnitario,
            total = total,
            fecha = fecha,
            client = cliente,
            producto = producto
        )

        return pedidosRepository.save(pedido)
    }


    override fun deleteById(id: Long) = pedidosRepository.deleteById(id)

    override fun findByClienteId(clienteId: Long): List<Pedido> =
        pedidosRepository.findAll().filter { it.clienteId == clienteId }
}
