package dev.javi.rugs_903_back.dto

import jakarta.validation.constraints.*

data class RegisterRequest(

    @field:NotBlank
    @field:Email(message = "El email no es válido")
    val email: String,

    @field:NotBlank
    @field:Size(min = 3, message = "El nombre de usuario debe tener al menos 3 caracteres")
    val username: String,

    @field:NotBlank
    @field:Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    val password: String,

    val rol: String = "user", // siempre registramos como user

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val surname: String,

    @field:NotBlank
    val phoneNumber: String,

    val address: AddressDtoForRegister
)

data class AddressDtoForRegister(
    @field:NotBlank
    val calle: String,

    @field:NotBlank
    val numero: String,

    @field:NotBlank
    val portal: String,

    @field:NotBlank
    val piso: String,

    @field:NotBlank
    val codigoPostal: String,

    @field:NotBlank
    val ciudad: String,

    @field:NotBlank
    val provincia: String
)
