package dev.javi.rugs_903_back.models

import jakarta.persistence.*


@Entity
@Table(name = "clients")
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @OneToOne(cascade = [CascadeType.ALL]) // Un cliente tiene una dirección
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    val address: Direccion,

    val phoneNumber: String,

    val name: String,

    val surname: String,

    @OneToOne // Un cliente tiene un usuario
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL]) // Un cliente tiene múltiples pedidos
    val pedidos: List<Pedido> = mutableListOf()


)
