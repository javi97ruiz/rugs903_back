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
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/users/me", "/users/me/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/users/admin", "/users/admin/**").hasRole("ADMIN")
                    .requestMatchers("/pedidos/me", "/pedidos/me/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/pedidos/admin", "/pedidos/admin/**").hasRole("ADMIN")
                    .requestMatchers("/pedidos", "/pedidos/**").hasRole("ADMIN")
                    .requestMatchers("/clients/**").authenticated()

                    // ✅ PRODUCTS → primero las de admin:
                    .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                    // ✅ CUSTOM PRODUCTS → igual:
                    .requestMatchers(HttpMethod.POST, "/custom-products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/custom-products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/custom-products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/custom-products/**").permitAll()

                    .anyRequest().authenticated()
            }

            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOriginPatterns = listOf("https://rugs903-front.onrender.com")
            allowedMethods = listOf("*")
            allowedHeaders = listOf("*")
            exposedHeaders = listOf("Authorization")
            allowCredentials = true
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
