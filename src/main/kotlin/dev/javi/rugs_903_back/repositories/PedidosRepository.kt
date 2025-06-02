package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PedidosRepository : JpaRepository<Pedido, Long> {
}