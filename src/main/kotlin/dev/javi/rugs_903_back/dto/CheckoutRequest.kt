package dev.javi.rugs_903_back.dto

// dto/CheckoutRequest.kt
data class CheckoutItem(
    val productName: String,
    val quantity: Int,
    val unitAmount: Long
)

data class CheckoutRequest(
    val items: List<CheckoutItem>
)
