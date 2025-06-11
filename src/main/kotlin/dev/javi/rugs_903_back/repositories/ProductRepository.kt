package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByIsActiveTrue(): List<Product>
    fun findAllByIsActiveFalse(): List<Product>

}