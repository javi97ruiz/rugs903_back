package dev.javi.rugs_903_back.models

import jakarta.persistence.*

@Entity
@Table(name = "pedidos")
data class Pedido(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    val client: Client,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val producto: Product,

    @Column
    val cantidad: Int = 0,

    @Column
    val precioUnitario: Double = 0.0,

    @Column
    val total: Double = 0.0,

    @Column
    val fecha: String = ""
)
