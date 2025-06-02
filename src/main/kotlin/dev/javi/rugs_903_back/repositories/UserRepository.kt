package dev.javi.rugs_903_back.repositories

import dev.javi.rugs_903_back.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByUsername(username: String): User?
}