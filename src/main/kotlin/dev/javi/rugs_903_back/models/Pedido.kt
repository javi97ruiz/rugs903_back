package dev.javi.rugs_903_back.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "pedidos")
data class Pedido(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonManagedReference
    val client: Client,

    @Column
    val fecha: String = "",

    @Column
    var estado: String = "pendiente", // valores posibles: pendiente, enviado, cancelado

    @OneToMany(mappedBy = "pedido", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val lineas: MutableList<PedidoLinea> = mutableListOf(),

    @OneToMany(mappedBy = "pedido", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val customProducts: List<CustomProduct> = emptyList()
)

