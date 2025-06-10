package dev.javi.rugs_903_back.services

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import dev.javi.rugs_903_back.dto.CheckoutItem
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class StripeService {

    @PostConstruct
    fun init() {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY")
    }

    fun createCheckoutSession(
        items: List<CheckoutItem>,
        successUrl: String,
        cancelUrl: String,
        userId: Long,
        productosJson: String
    ): String {
        // ⚠️ Por seguridad volvemos a setear aquí también (doble seguro)
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY")

        println("👉 Creando sesión Stripe para userId=$userId")

        val lineItems = items.map {
            SessionCreateParams.LineItem.builder()
                .setQuantity(it.quantity.toLong())
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur")
                        .setUnitAmount(it.unitAmount)
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(it.productName)
                                .build()
                        )
                        .build()
                )
                .build()
        }

        val params = SessionCreateParams.builder()
            .addAllLineItem(lineItems)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .putMetadata("userId", userId.toString()) // 👈 metadatos para el webhook
            .putMetadata("productos", productosJson)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .build()

        val session = Session.create(params)

        println("👉 Session URL creada: ${session.url}")

        return session.url
    }
}

