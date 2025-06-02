// controllers/StripeController.kt
package dev.javi.rugs_903_back.controllers

import dev.javi.rugs_903_back.dto.CheckoutRequest
import dev.javi.rugs_903_back.services.StripeService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/api/payment")
class StripeController(
    private val stripeService: StripeService
) {

    @PostMapping("/checkout")
    fun createCheckoutSession(@RequestBody request: CheckoutRequest): ResponseEntity<Map<String, String>> {
        val url = stripeService.createCheckoutSession(
            items = request.items,
            successUrl = "http://localhost:5173/success",
            cancelUrl = "http://localhost:5173/cancel"
        )
        return ResponseEntity.ok(mapOf("url" to url))
    }

}
