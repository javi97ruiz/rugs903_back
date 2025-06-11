package dev.javi.rugs_903_back.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Entity
@Table(name = "clients")
class Client(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    val address: Direccion,

    @field:Pattern(
        regexp = "^[6-9][0-9]{8}$",
        message = "El número de teléfono debe tener 9 cifras y comenzar por 6, 7, 8 o 9"
    )
    val phoneNumber: String,

    @field:NotBlank(message = "El nombre es obligatorio")
    val name: String,

    @field:NotBlank(message = "Los apellidos son obligatorios")
    val surname: String,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL])
    @JsonBackReference
    val pedidos: List<Pedido> = mutableListOf()

)
 {

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

    fun copy(
        id: Long = this.id,
        address: Direccion = this.address,
        phoneNumber: String = this.phoneNumber,
        name: String = this.name,
        surname: String = this.surname,
        user: User = this.user,
        pedidos: List<Pedido> = this.pedidos
    ): Client {
        return Client(
            id = id,
            address = address,
            phoneNumber = phoneNumber,
            name = name,
            surname = surname,
            user = user,
            pedidos = pedidos
        )
    }

}
