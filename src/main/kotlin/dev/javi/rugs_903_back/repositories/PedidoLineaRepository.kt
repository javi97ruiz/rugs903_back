package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.PedidoLinea
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PedidoLineaRepository : JpaRepository<PedidoLinea, Long>
