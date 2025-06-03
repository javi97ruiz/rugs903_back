package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.Client
import org.springframework.data.jpa.repository.JpaRepository

interface ClientRepository : JpaRepository<Client, Long> {
    fun findByUserUsername(username: String): Client?

}