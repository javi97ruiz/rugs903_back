package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.stripe.model.Event
import com.stripe.net.Webhook
import dev.javi.rugs_903_back.dto.PedidoCreateRequestDto
import dev.javi.rugs_903_back.services.PedidoService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.util.ReflectionTestUtils

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class StripeWebhookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var pedidoService: PedidoService

    // --------- /stripe/webhook ------------

    @Test
    fun `handleStripeWebhook should return OK for non checkout session completed event`() {
        val payload = buildNonCheckoutEventPayload()
        val sigHeader = "test_signature"

        mockStatic(Webhook::class.java).use { mockedWebhook ->
            val event = Event().apply {
                type = "payment_intent.succeeded"
            }

            `when`(Webhook.constructEvent(payload, sigHeader, System.getenv("STRIPE_WEBHOOK_SECRET"))).thenReturn(event)

            mockMvc.perform(
                post("/stripe/webhook")
                    .header("Stripe-Signature", sigHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
                .andExpect(status().isOk)
                .andExpect(content().string("Evento recibido"))

            verifyNoInteractions(pedidoService)
        }
    }

    @Test
    fun `handleStripeWebhook should return 400 on exception`() {
        val payload = "invalid_payload"
        val sigHeader = "test_signature"

        mockStatic(Webhook::class.java).use { mockedWebhook ->
            `when`(Webhook.constructEvent(payload, sigHeader, System.getenv("STRIPE_WEBHOOK_SECRET")))
                .thenThrow(RuntimeException("Invalid payload"))

            mockMvc.perform(
                post("/stripe/webhook")
                    .header("Stripe-Signature", sigHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().string("Webhook inválido"))

            verifyNoInteractions(pedidoService)
        }
    }

    // --------- Helpers ------------

    private fun buildNonCheckoutEventPayload(): String {
        val objectMapper = jacksonObjectMapper()

        val payloadMap = mapOf(
            "data" to mapOf(
                "object" to mapOf(
                    "metadata" to emptyMap<String, Any>()
                )
            )
        )

        val eventMap = mapOf(
            "type" to "payment_intent.succeeded",
            "data" to payloadMap["data"]
        )

        return objectMapper.writeValueAsString(eventMap)
    }
}
