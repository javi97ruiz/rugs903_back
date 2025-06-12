package dev.javi.rugs_903_back.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "pedidos")
class Pedido(

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
    var estado: String = "pendiente", // pendiente, enviado, cancelado

    @OneToMany(mappedBy = "pedido", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val lineas: MutableList<PedidoLinea> = mutableListOf(),

    @OneToMany(mappedBy = "pedido", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val customProducts: List<CustomProduct> = emptyList()

) {

    override fun toString(): String {
        return "Pedido(id=$id, fecha='$fecha', estado='$estado')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pedido) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun copy(
        id: Long = this.id,
        client: Client = this.client,
        fecha: String = this.fecha,
        estado: String = this.estado,
        lineas: MutableList<PedidoLinea> = this.lineas,
        customProducts: List<CustomProduct> = this.customProducts
    ): Pedido {
        return Pedido(
            id = id,
            client = client,
            fecha = fecha,
            estado = estado,
            lineas = lineas,
            customProducts = customProducts
        )
    }

}
