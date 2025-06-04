package dev.javi.rugs_903_back.models

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @CreatedDate
    @Column(updatable = false)
    val createdAt: LocalDateTime,

    @LastModifiedDate
    @Column()
    val updatedAt: LocalDateTime,

    @Column
    val isActive: Boolean = true,

    @Column(nullable = false)
    val rol: String // "user" o "admin"

)