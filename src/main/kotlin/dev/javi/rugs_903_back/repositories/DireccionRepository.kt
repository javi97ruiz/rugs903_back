package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.Direccion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DireccionRepository: JpaRepository<Direccion, Long> {
}