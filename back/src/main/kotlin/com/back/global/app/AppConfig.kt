package com.back.global.app

import com.back.standard.util.Ut
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object AppConfig {
    lateinit var customConfigProperties: CustomConfigProperties

    val siteFrontUrl: String by lazy { customConfigProperties.site.frontUrl }
    val siteBackUrl: String by lazy { customConfigProperties.site.backUrl }
    val siteCookieDomain: String by lazy { customConfigProperties.site.cookieDomain }
    val siteName: String by lazy { customConfigProperties.site.name }
    val jwtSecretKey: String by lazy { customConfigProperties.jwt.secretKey }
    val accessTokenExpirationSeconds: Long by lazy { customConfigProperties.accessToken.expirationSeconds }
}

@Configuration
class AppConfigInitializer(
    private val customConfigProperties: CustomConfigProperties
) {
    @Autowired(required = false)
    private var objectMapper: ObjectMapper? = null
    @PostConstruct
    fun init() {
        Ut.json.objectMapper = objectMapper ?: ObjectMapper()
        AppConfig.customConfigProperties = customConfigProperties
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
