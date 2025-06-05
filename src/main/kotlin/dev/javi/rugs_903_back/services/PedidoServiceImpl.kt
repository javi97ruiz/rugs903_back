package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.dto.PedidoRequestDto
import dev.javi.rugs_903_back.models.Pedido
import dev.javi.rugs_903_back.repositories.ClientRepository
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
    private val productRepository: ProductRepository
) : PedidoService {

    override fun findAll(): List<Pedido> = pedidosRepository.findAll()

    override fun findById(id: Long): Pedido? = pedidosRepository.findById(id).orElse(null)

    override fun save(dto: PedidoRequestDto): Pedido {
        val cliente = clientRepository.findById(dto.clienteId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado") }

        val producto = productRepository.findById(dto.productId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado") }

        val precioUnitario = producto.price
        val total = precioUnitario * dto.cantidad
        val fecha = LocalDate.now().toString()

        val pedido = Pedido(
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
}
