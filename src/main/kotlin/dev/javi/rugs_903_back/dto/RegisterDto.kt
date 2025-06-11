// dto/RegisterRequest.kt
package dev.javi.rugs_903_back.dto

data class RegisterRequest(
    val username: String,
    val password: String,
    val rol: String = "user", // siempre registramos como user
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val address: AddressDtoForRegister
)

data class AddressDtoForRegister(
    val calle: String,
    val numero: String,
    val portal: String,
    val piso: String,
    val codigoPostal: String,
    val ciudad: String,
    val provincia: String
)
