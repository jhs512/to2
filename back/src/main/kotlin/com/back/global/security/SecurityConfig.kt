package com.back.global.security

import com.back.global.app.AppConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customAuthenticationFilter: CustomAuthenticationFilter,
    private val customOAuth2LoginSuccessHandler: CustomOAuth2LoginSuccessHandler,
    private val customOAuth2AuthorizationRequestResolver: CustomOAuth2AuthorizationRequestResolver
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(HttpMethod.DELETE, "/api/v1/members/auth", permitAll)
                authorize("/api/v1/**", authenticated)
                authorize(anyRequest, permitAll)
            }

            headers {
                frameOptions { sameOrigin = true }
            }

            csrf { disable() }
            formLogin { disable() }
            logout { disable() }
            httpBasic { disable() }

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            oauth2Login {
                authenticationSuccessHandler = customOAuth2LoginSuccessHandler

                authorizationEndpoint {
                    authorizationRequestResolver = customOAuth2AuthorizationRequestResolver
                }
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(customAuthenticationFilter)
        }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("https://cdpn.io", AppConfig.siteFrontUrl)
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")
            allowCredentials = true
            allowedHeaders = listOf("*")
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/api/**", configuration)
        }
    }
}
