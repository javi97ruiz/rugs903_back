package dev.javi.rugs_903_back.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Entity
@Table(name = "direcciones")
data class Direccion(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:NotBlank(message = "La calle es obligatoria")
    val calle: String,

    @field:NotBlank(message = "El número es obligatorio")
    val numero: String,

    @field:NotBlank(message = "El portal es obligatorio")
    val portal: String,

    @field:NotBlank(message = "El piso es obligatorio")
    val piso: String,

    @field:Pattern(
        regexp = "^[0-9]{5}$",
        message = "El código postal debe tener 5 cifras"
    )
    val codigoPostal: String,

    @field:NotBlank(message = "La ciudad es obligatoria")
    val ciudad: String,

    @field:NotBlank(message = "La provincia es obligatoria")
    val provincia: String
)
