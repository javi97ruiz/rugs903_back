package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.CustomProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomProductRepository : JpaRepository<CustomProduct, Long>

