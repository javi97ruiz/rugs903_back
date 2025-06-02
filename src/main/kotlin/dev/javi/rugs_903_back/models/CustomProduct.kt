package dev.javi.rugs_903_back.models

import jakarta.persistence.*

@Entity
data class CustomProduct (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val height: Int,
    val length: Int,
    val imageUrl: String

)