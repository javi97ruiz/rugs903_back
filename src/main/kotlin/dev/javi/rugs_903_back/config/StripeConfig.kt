// config/StripeConfig.kt
package dev.javi.rugs_903_back.config

import com.stripe.Stripe
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class StripeConfig(
    @Value("\${stripe.secret-key}")
    private val stripeSecretKey: String
) {
    @PostConstruct
    fun init() {
        Stripe.apiKey = stripeSecretKey
    }
}
