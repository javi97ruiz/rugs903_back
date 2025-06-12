package dev.javi.rugs_903_back.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.javi.rugs_903_back.dto.CheckoutItem
import dev.javi.rugs_903_back.dto.CheckoutRequest
import dev.javi.rugs_903_back.services.StripeService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class StripeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var stripeService: StripeService

    // --------- /payment/checkout ------------

    @Test
    fun `createCheckoutSession should return 200 when successful`() {
        val request = buildCheckoutRequest()

        `when`(stripeService.createCheckoutSession(
            items = request.items,
            successUrl = "https://rugs903-front.onrender.com/success",
            cancelUrl = "https://rugs903-front.onrender.com/cancel",
            userId = request.userId,
            productosJson = request.productos
        )).thenReturn("https://checkout.stripe.com/test-session-url")

        mockMvc.perform(
            post("/payment/checkout")
                .with(user("user").roles("USER")) // requiere autenticación por configuración actual
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.url").value("https://checkout.stripe.com/test-session-url"))
    }

    @Test
    fun `createCheckoutSession should return 500 when exception occurs`() {
        val request = buildCheckoutRequest()

        `when`(stripeService.createCheckoutSession(
            items = request.items,
            successUrl = "https://rugs903-front.onrender.com/success",
            cancelUrl = "https://rugs903-front.onrender.com/cancel",
            userId = request.userId,
            productosJson = request.productos
        )).thenThrow(RuntimeException("Stripe error"))

        mockMvc.perform(
            post("/payment/checkout")
                .with(user("user").roles("USER")) // requiere autenticación por configuración actual
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("Stripe error"))
    }

    // --------- Helper ------------

    private fun buildCheckoutRequest(): CheckoutRequest {
        return CheckoutRequest(
            items = listOf(
                CheckoutItem(
                    productName = "Producto Test",
                    quantity = 2,
                    unitAmount = 2500
                )
            ),
            userId = 1L,
            productos = """{"productos":[{"id":1,"nombre":"Producto Test","cantidad":2,"precio":25.00}]}"""
        )
    }
}
