package dev.javi.rugs_903_back.models

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:NotBlank(message = "El nombre del producto es obligatorio")
    val name: String = "",

    @field:NotBlank(message = "La descripción del producto es obligatoria")
    val description: String = "",

    @field:Min(value = 0, message = "El precio no puede ser negativo")
    val price: Double = 0.0,

    @field:Min(value = 0, message = "La cantidad no puede ser negativa")
    val quantity: Int = 0,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @field:NotBlank(message = "La imagen del producto es obligatoria")
    @Column(columnDefinition = "text")
    val imagen: String = ""
)
