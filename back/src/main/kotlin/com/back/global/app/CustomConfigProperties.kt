package com.back.global.app

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "custom")
class CustomConfigProperties {
    val site = Site()
    val jwt = Jwt()
    val accessToken = AccessToken()
    var notProdMembers: MutableList<NotProdMember> = mutableListOf()

    class Site {
        var frontUrl: String = ""
        var backUrl: String = ""
        var cookieDomain: String = ""
        var name: String = ""
    }

    class Jwt {
        var secretKey: String = ""
    }

    class AccessToken {
        var expirationSeconds: Long = 0
    }

    data class NotProdMember(
        val username: String = "",
        val apiKey: String = "",
        val nickname: String = "",
        val profileImgUrl: String = ""
    )
}
