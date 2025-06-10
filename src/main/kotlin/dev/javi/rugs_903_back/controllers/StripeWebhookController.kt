package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import dev.javi.rugs_903_back.dto.PedidoRequestDto
import dev.javi.rugs_903_back.services.PedidoService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stripe")
class StripeWebhookController(
    private val pedidoService: PedidoService
) {

    @PostMapping("/webhook")
    fun handleStripeWebhook(@RequestBody payload: String, request: HttpServletRequest): ResponseEntity<String> {
        val sigHeader = request.getHeader("Stripe-Signature")
        val endpointSecret = System.getenv("STRIPE_WEBHOOK_SECRET")

        return try {
            val event = Webhook.constructEvent(payload, sigHeader, endpointSecret)

            println("🔔 Recibido evento Stripe: ${event.type}")

            if (event.type == "checkout.session.completed") {
                val optionalObject = event.dataObjectDeserializer.`object`

                if (optionalObject.isPresent) {
                    val rawObject = optionalObject.get()
                    println("👉 Tipo de object recibido: ${rawObject.javaClass.name}")

                    val userId: Long?
                    val productosJson: String

                    if (rawObject is Session) {
                        val session = rawObject
                        println("✅ Pago completado: ${session.id}")

                        userId = session.metadata["userId"]?.toLongOrNull()
                        productosJson = session.metadata["productos"] ?: "[]"

                    } else if (rawObject is Map<*, *>) {
                        // Fallback → parsear manualmente el Map
                        val metadata = (rawObject["metadata"] as? Map<String, String>) ?: emptyMap()
                        userId = metadata["userId"]?.toLongOrNull()
                        productosJson = metadata["productos"] ?: "[]"

                        println("✅ [Fallback] Metadata recibida manualmente: userId=$userId productos=$productosJson")
                    } else {
                        println("⚠️ WARNING: object no es Session ni Map, tipo: ${rawObject.javaClass.name}")
                        return ResponseEntity.ok("Evento recibido (sin procesar)")
                    }

                    // Validación de userId
                    if (userId == null) {
                        println("⚠️ ERROR: userId nulo en metadata, no se puede procesar el pedido.")
                        return ResponseEntity.status(400).body("userId nulo en metadata")
                    }

                    // Parseamos productosJson
                    val objectMapper = jacksonObjectMapper()
                    val productosList: List<Map<String, Any>> = objectMapper.readValue(
                        productosJson,
                        object : com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Any>>>() {}
                    )

                    // Por cada producto → crear pedido individual
                    productosList.forEach { productoMap ->
                        val productId = (productoMap["id"] as Number).toLong()
                        val cantidad = (productoMap["cantidad"] as Number).toInt()

                        val pedidoDto = PedidoRequestDto(
                            clienteId = userId,
                            productId = productId,
                            cantidad = cantidad,
                            customProductIds = emptyList()
                        )

                        println("👉 Guardando pedido: $pedidoDto")
                        pedidoService.save(pedidoDto)
                    }

                    return ResponseEntity.ok("Pedidos procesados")
                } else {
                    println("⚠️ WARNING: Event data.object no presente en el evento ${event.type}")
                }
            }

            ResponseEntity.ok("Evento recibido")
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(400).body("Webhook inválido")
        }
    }
}
