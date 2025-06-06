package dev.javi.rugs_903_back.config

import com.cloudinary.Cloudinary
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudinaryConfig {

    @Value("\${cloudinary.cloud_name}")
    lateinit var cloudName: String

    @Value("\${cloudinary.api_key}")
    lateinit var apiKey: String

    @Value("\${cloudinary.api_secret}")
    lateinit var apiSecret: String

    @Bean
    fun cloudinary(): Cloudinary {
        val config = mapOf(
            "cloud_name" to cloudName,
            "api_key" to apiKey,
            "api_secret" to apiSecret,
            "secure" to true
        )
        return Cloudinary(config)
    }
}
