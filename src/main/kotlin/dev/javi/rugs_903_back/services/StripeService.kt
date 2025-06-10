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
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY") // <- la cogeremos de las env vars
    }

    fun createCheckoutSession(
        items: List<CheckoutItem>,
        successUrl: String,
        cancelUrl: String,
        userId: Long,
        productosJson: String
    ): String {
        val stripeKey = System.getenv("STRIPE_SECRET_KEY")
        println("👉 Stripe SECRET KEY: $stripeKey")  // LOG para ver si llega

        Stripe.apiKey = stripeKey ?: throw IllegalStateException("Stripe Secret Key no configurada.")

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
            .putMetadata("userId", userId.toString())
            .putMetadata("productos", productosJson)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .addExpand("payment_intent")  // opcional
            .addExpand("line_items")      // importante si quieres productos
            .build()

        println("👉 Creando sesión de pago en Stripe...")  // LOG extra

        val session = Session.create(params)
        println("👉 Session URL creada: ${session.url}")   // LOG extra

        return session.url
    }

}
