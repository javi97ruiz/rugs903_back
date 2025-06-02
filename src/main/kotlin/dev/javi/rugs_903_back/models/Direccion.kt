package dev.javi.rugs_903_back.models

import jakarta.persistence.*

@Entity
@Table(name = "direccion")
data class Direccion (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val calle: String = "",

    @Column(nullable = false)
    val numero: String = "",

    @Column(nullable = false)
    val portal: String = "",

    @Column(nullable = false)
    val piso: String = "",

    @Column(nullable = false)
    val codigoPostal: String = "",

    @Column(nullable = false)
    val ciudad: String = "",

    @Column(nullable = false)
    val provincia: String = ""

)
