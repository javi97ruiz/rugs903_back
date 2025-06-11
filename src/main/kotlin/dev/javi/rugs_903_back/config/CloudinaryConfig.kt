package dev.javi.rugs_903_back.config

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudinaryConfig {

    @Value("\${cloudinary.cloud_name}")
    private lateinit var cloudName: String

    @Value("\${cloudinary.api_key}")
    private lateinit var apiKey: String

    @Value("\${cloudinary.api_secret}")
    private lateinit var apiSecret: String

    @Bean
    fun cloudinary(): Cloudinary {
        return Cloudinary(
            ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
            )
        )
    }
}
