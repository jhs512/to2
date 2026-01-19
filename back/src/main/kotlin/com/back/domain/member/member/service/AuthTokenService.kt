package com.back.domain.member.member.service

import com.back.domain.member.member.entity.Member
import com.back.global.app.AppConfig
import com.back.standard.util.Ut
import org.springframework.stereotype.Service

@Service
class AuthTokenService {

    fun genAccessToken(member: Member): String {
        return Ut.JWT.createToken(
            AppConfig.jwtSecretKey,
            AppConfig.accessTokenExpirationSeconds,
            mapOf(
                "id" to member.id,
                "username" to member.username,
                "name" to member.name
            )
        )
    }

    fun payload(accessToken: String): Map<String, Any>? {
        return Ut.JWT.payload(AppConfig.jwtSecretKey, accessToken)
    }
}
