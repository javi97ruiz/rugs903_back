// controllers/StripeController.kt
package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.CheckoutRequest
import dev.javi.rugs_903_back.services.StripeService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/payment")  // ya sin /api, bien
class StripeController(
    private val stripeService: StripeService
) {

    @PostMapping("/checkout")
    fun createCheckoutSession(@RequestBody request: CheckoutRequest): ResponseEntity<Map<String, String>> {
        return try {
            val frontendUrl = System.getenv("FRONTEND_URL") ?: "https://rugs903-front.onrender.com"

            val url = stripeService.createCheckoutSession(
                items = request.items,
                successUrl = "$frontendUrl/success",
                cancelUrl = "$frontendUrl/cancel",
                userId = request.userId,
                productosJson = request.productos
            )
            ResponseEntity.ok(mapOf("url" to url))
        } catch (e: Exception) {
            e.printStackTrace()  // <--- Imprime el error de Stripe
            ResponseEntity.status(500).body(mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}

