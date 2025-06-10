package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.stripe.net.Webhook
import dev.javi.rugs_903_back.dto.PedidoCreateRequestDto
import dev.javi.rugs_903_back.dto.PedidoLineaRequestDto
import dev.javi.rugs_903_back.models.CustomProduct
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
                val objectMapper = jacksonObjectMapper()
                val jsonNode = objectMapper.readTree(payload)
                val metadataNode = jsonNode["data"]["object"]["metadata"]

                val userId = metadataNode?.get("userId")?.asText()?.toLongOrNull()
                val productosJson = metadataNode?.get("productos")?.asText() ?: "[]"

                println("👉 Metadata recibida (manual parsing): userId=$userId, productos=$productosJson")

                if (userId == null) {
                    println("⚠️ ERROR: userId nulo en metadata, no se puede procesar el pedido.")
                    return ResponseEntity.status(400).body("userId nulo en metadata")
                }

                // Parseamos productosJson a lista de Map
                val productosList: List<Map<String, Any>> = objectMapper.readValue(
                    productosJson,
                    object : com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Any>>>() {}
                )

                // 1️⃣ PedidoLineaRequestDto solo para productos normales
                val lineasPedido = productosList.mapNotNull { productoMap ->
                    val rawId = productoMap["id"]
                    val cantidad = (productoMap["cantidad"] as Number).toInt()

                    if (rawId is Number) {
                        PedidoLineaRequestDto(
                            productId = rawId.toLong(),
                            cantidad = cantidad
                        )
                    } else {
                        null
                    }
                }

                // 2️⃣ Productos personalizados (CustomProduct)
                val customProducts = productosList.mapNotNull { productoMap ->
                    val rawId = productoMap["id"]

                    if (rawId is String && rawId.startsWith("custom-")) {
                        // Aquí tú decides qué campos vas a guardar del producto personalizado
                        // De momento el JSON de Stripe solo tiene id/cantidad/precio
                        // Puedes expandir el metadata si quieres más info en el futuro
                        CustomProduct(
                            name = "Producto personalizado", // puedes meterlo en metadata si quieres el name real
                            height = 0, // aquí también puedes expandir el metadata
                            length = 0,
                            imageUrl = "", // si pones la URL en metadata, puedes mapearla aquí
                            pedido = null // lo asignará el Service cuando guardes el Pedido
                        )
                    } else {
                        null
                    }
                }

                val pedidoDto = PedidoCreateRequestDto(
                    clienteId = userId,
                    lineas = lineasPedido,
                    estado = "PAGADO" // o "PENDIENTE", como prefieras
                )

                println("👉 Guardando pedido con lineas: $pedidoDto")
                println("👉 Productos personalizados a guardar: $customProducts")

                // Aquí te toca adaptar el Service:
                // savePedidoConLineas(pedidoDto, customProducts)
                // o bien primero guardar el Pedido, luego insertar los CustomProducts con el pedido asignado.

                pedidoService.savePedidoConLineas(pedidoDto)

                return ResponseEntity.ok("Pedido con líneas y custom products procesado")
            }

            ResponseEntity.ok("Evento recibido")
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.status(400).body("Webhook inválido")
        }
    }

}
