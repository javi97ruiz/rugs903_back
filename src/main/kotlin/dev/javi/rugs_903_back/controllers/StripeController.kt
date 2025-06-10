package dev.javi.rugs_903_back.controllers

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import dev.javi.rugs_903_back.dto.PedidoRequestDto
import dev.javi.rugs_903_back.services.PedidoService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/payment")
class StripeController(
    private val pedidoService: PedidoService
) {

    @Value("\${STRIPE_SECRET_KEY}")
    private lateinit var stripeSecretKey: String

    @PostMapping("/checkout")
    fun checkout(@RequestBody body: Map<String, Any>): ResponseEntity<Map<String, String>> {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY")

        val items = body["items"] as List<Map<String, Any>>
        val productos = body["productos"] as String
        val userId = (body["userId"] as Number).toLong()

        val lineItems = items.map {
            SessionCreateParams.LineItem.builder()
                .setQuantity((it["quantity"] as Number).toLong())
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur")
                        .setUnitAmount((it["unitAmount"] as Number).toLong())
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(it["productName"] as String)
                                .build()
                        )
                        .build()
                )
                .build()
        }

        val session = Session.create(
            SessionCreateParams.builder()
                .addAllLineItem(lineItems)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://rugs903-front.onrender.com/success")
                .setCancelUrl("https://rugs903-front.onrender.com/cancel")
                .build()
        )

        // 🧠 Aquí parseamos los productos y guardamos los pedidos
        val objectMapper = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper()
        val productosList: List<Map<String, Any>> = objectMapper.readValue(
            productos,
            object : com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Any>>>() {}
        )

        productosList.forEach { productoMap ->
            val pedidoDto = PedidoRequestDto(
                clienteId = userId,
                productId = (productoMap["id"] as Number).toLong(),
                cantidad = (productoMap["cantidad"] as Number).toInt(),
                customProductIds = emptyList()
            )
            println("📦 Creando pedido: $pedidoDto")
            pedidoService.save(pedidoDto, estado = "PENDIENTE")
        }

        return ResponseEntity.ok(mapOf("url" to session.url))
    }

}
