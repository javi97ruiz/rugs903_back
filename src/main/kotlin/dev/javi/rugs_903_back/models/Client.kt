package dev.javi.rugs_903_back.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*


@Entity
@Table(name = "clients")
class Client(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    val address: Direccion,

    val phoneNumber: String,

    val name: String,

    val surname: String,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL])
    @JsonBackReference
    val pedidos: List<Pedido> = mutableListOf()

) {

    override fun toString(): String {
        return "Client(id=$id, name='$name', surname='$surname', phoneNumber='$phoneNumber')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Client) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
