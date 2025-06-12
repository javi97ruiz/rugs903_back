package dev.javi.rugs_903_back.services

import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import dev.javi.rugs_903_back.dto.CheckoutItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class StripeServiceTest {

    private val stripeService = StripeService()

    @Test
    fun `createCheckoutSession should create session and return URL`() {
        val items = listOf(
            CheckoutItem(productName = "Test Product", quantity = 2, unitAmount = 1000L)
        )
        val successUrl = "https://example.com/success"
        val cancelUrl = "https://example.com/cancel"
        val userId = 123L
        val productosJson = """{"items":[]}"""

        // Mock de la Session de Stripe
        val mockSession = Mockito.mock(Session::class.java)
        Mockito.`when`(mockSession.url).thenReturn("https://mocked.session.url")

        // Mock del método estático Session.create
        val mockedStatic: MockedStatic<Session> = Mockito.mockStatic(Session::class.java)
        mockedStatic.use { sessionMock ->
            sessionMock.`when`<Session> {
                Session.create(Mockito.any(SessionCreateParams::class.java))
            }.thenReturn(mockSession)

            val resultUrl = stripeService.createCheckoutSession(
                items = items,
                successUrl = successUrl,
                cancelUrl = cancelUrl,
                userId = userId,
                productosJson = productosJson
            )

            // Verificar que Session.create fue llamado
            sessionMock.verify {
                Session.create(Mockito.any(SessionCreateParams::class.java))
            }

            // Verificar que la URL devuelta es la que esperamos
            assertEquals("https://mocked.session.url", resultUrl)
        }
    }
}
