package dev.javi.rugs_903_back.models

import jakarta.persistence.*

@Entity
@Table(name = "products")
data class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,

)