package com.back.global.app

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig(
    @Value("\${custom.site.frontUrl}") val siteFrontUrl: String,
    @Value("\${custom.site.backUrl}") val siteBackUrl: String,
    @Value("\${custom.site.cookieDomain}") val siteCookieDomain: String,
    @Value("\${custom.site.name}") val siteName: String,
    @Value("\${custom.jwt.secretKey}") val jwtSecretKey: String,
    @Value("\${custom.accessToken.expirationSeconds}") val accessTokenExpirationSeconds: Long
) {
    companion object {
        lateinit var siteFrontUrl: String
        lateinit var siteBackUrl: String
        lateinit var siteCookieDomain: String
        lateinit var siteName: String
        lateinit var jwtSecretKey: String
        var accessTokenExpirationSeconds: Long = 0
    }

    @PostConstruct
    fun init() {
        Companion.siteFrontUrl = siteFrontUrl
        Companion.siteBackUrl = siteBackUrl
        Companion.siteCookieDomain = siteCookieDomain
        Companion.siteName = siteName
        Companion.jwtSecretKey = jwtSecretKey
        Companion.accessTokenExpirationSeconds = accessTokenExpirationSeconds
    }
}
