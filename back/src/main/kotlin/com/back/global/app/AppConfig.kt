package com.back.global.app

import com.back.standard.util.Ut
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import tools.jackson.databind.ObjectMapper

object AppConfig {
    private lateinit var ctx: ApplicationContext

    internal fun init(applicationContext: ApplicationContext) {
        ctx = applicationContext
    }

    private inline fun <reified T : Any> bean(): T = ctx.getBean<T>()

    val props: CustomConfigProperties by lazy { bean() }

    val siteFrontUrl: String get() = props.site.frontUrl
    val siteBackUrl: String get() = props.site.backUrl
    val siteCookieDomain: String get() = props.site.cookieDomain
    val siteName: String get() = props.site.name
    val jwtSecretKey: String get() = props.jwt.secretKey
    val accessTokenExpirationSeconds: Long get() = props.accessToken.expirationSeconds
}

@Configuration
private class AppConfigInitializer(
    applicationContext: ApplicationContext,
    private val objectMapper: ObjectMapper
) {
    init {
        AppConfig.init(applicationContext)

        Ut.JSON.objectMapper = objectMapper
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}