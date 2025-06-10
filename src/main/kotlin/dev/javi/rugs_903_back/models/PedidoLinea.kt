package dev.javi.rugs_903_back.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "pedido_lineas")
class PedidoLinea(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    val pedido: Pedido,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val producto: Product,

    @Column
    val cantidad: Int = 0,

    @Column
    val precioUnitario: Double = 0.0,

    @Column
    val total: Double = 0.0

) {

    override fun toString(): String {
        return "PedidoLinea(id=$id, cantidad=$cantidad, precioUnitario=$precioUnitario, total=$total)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PedidoLinea) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
