package dev.javi.rugs_903_back.config

import dev.javi.rugs_903_back.security.JwtTokenFilter
import dev.javi.rugs_903_back.services.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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

@Configuration
class SecurityConfig(
    private val jwtTokenFilter: JwtTokenFilter,
    private val userDetailsService: CustomUserDetailsService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(AntPathRequestMatcher("/auth/**")).permitAll()
                    .anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
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
