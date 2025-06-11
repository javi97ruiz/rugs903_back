package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.Client
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ClientRepository : JpaRepository<Client, Long> {
    fun findByUserUsername(username: String): Client?
    fun findByUserId(userId: Long): Optional<Client>
    fun findAllByIsActiveTrue(): List<Client>
    fun findAllByIsActiveFalse(): List<Client>


}