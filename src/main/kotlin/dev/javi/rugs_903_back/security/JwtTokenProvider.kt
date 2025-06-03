package dev.javi.rugs_903_back.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {

    private val secret = "super_secret_key" // 🔐 cámbialo y protégelo en producción
    private val expirationMs = 86400000 // 1 día

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(username: String, role: String): String {
        return JWT.create()
            .withSubject(username)
            .withClaim("role", "ROLE_${role.uppercase()}")
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + expirationMs))
            .sign(algorithm)
    }

    fun getUsernameFromToken(token: String): String {
        return JWT.require(algorithm)
            .build()
            .verify(token)
            .subject
    }

    fun getRoleFromToken(token: String): String {
        return JWT.require(algorithm)
            .build()
            .verify(token)
            .getClaim("role").asString()
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            JWT.require(algorithm).build().verify(token)
            true
        } catch (ex: Exception) {
            false
        }
    }
}
