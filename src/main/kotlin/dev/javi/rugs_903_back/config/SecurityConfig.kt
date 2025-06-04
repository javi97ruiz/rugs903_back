package dev.javi.rugs_903_back.config

import dev.javi.rugs_903_back.security.JwtTokenFilter
import dev.javi.rugs_903_back.services.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val jwtTokenFilter: JwtTokenFilter,
    private val userDetailsService: CustomUserDetailsService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 🟢 Añade esta línea
                    .requestMatchers("/auth/**").permitAll() // públicas
                    .requestMatchers("/users/**").hasRole("ADMIN") // solo admin
                    .requestMatchers("/pedidos").hasRole("ADMIN") // lista completa de pedidos solo admin
                    .requestMatchers("/pedidos/mis-pedidos").hasAnyRole("USER", "ADMIN") // pedidos personales
                    .requestMatchers("/clients/**").authenticated() // protegida, cualquier autenticado
                    .requestMatchers("/products/**", "/custom-products/**").permitAll() // pública
                    .anyRequest().authenticated() // el resto requiere autenticación
            }
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            //allowedOriginPatterns = listOf("https://rugs903-front.onrender.com")
            //allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            //allowedHeaders = listOf("Authorization", "Content-Type")
            exposedHeaders = listOf("Authorization") // <-- expone cabecera para lectura (opcional pero recomendable)
            allowCredentials = true
            allowedMethods = listOf("*")
            allowedHeaders = listOf("*")
            allowedOriginPatterns = listOf("*") // prueba así temporalmente

        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }



    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}
