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
    @Column(columnDefinition = "text")
    val imageUrl: String,
    @Column(nullable = false)
    val price: Double,    // 👈 AÑADIR ESTE CAMPO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = true)
    val pedido: Pedido? = null

)