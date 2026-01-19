package com.back.global.app

import com.back.standard.util.Ut
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object AppConfig {
    lateinit var siteFrontUrl: String
    lateinit var siteBackUrl: String
    lateinit var siteCookieDomain: String
    lateinit var siteName: String
    lateinit var jwtSecretKey: String
    var accessTokenExpirationSeconds: Long = 0
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
        AppConfig.siteFrontUrl = customConfigProperties.site.frontUrl
        AppConfig.siteBackUrl = customConfigProperties.site.backUrl
        AppConfig.siteCookieDomain = customConfigProperties.site.cookieDomain
        AppConfig.siteName = customConfigProperties.site.name
        AppConfig.jwtSecretKey = customConfigProperties.jwt.secretKey
        AppConfig.accessTokenExpirationSeconds = customConfigProperties.accessToken.expirationSeconds
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
