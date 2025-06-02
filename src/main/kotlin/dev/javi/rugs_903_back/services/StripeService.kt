// services/StripeService.kt
package dev.javi.rugs_903_back.services

import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import dev.javi.rugs_903_back.dto.CheckoutItem
import org.springframework.stereotype.Service

@Service
class StripeService {

    fun createCheckoutSession(
        items: List<CheckoutItem>,
        successUrl: String,
        cancelUrl: String
    ): String {
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
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .build()

        val session = Session.create(params)
        return session.url
    }

}
