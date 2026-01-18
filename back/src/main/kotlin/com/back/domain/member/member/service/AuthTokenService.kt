package com.back.domain.member.member.service

import com.back.domain.member.member.entity.Member
import com.back.global.app.AppConfig
import com.back.standard.util.Ut
import org.springframework.stereotype.Service

@Service
class AuthTokenService {

    fun genAccessToken(member: Member): String {
        return Ut.jwt.createToken(
            AppConfig.jwtSecretKey,
            AppConfig.accessTokenExpirationSeconds,
            mapOf(
                "id" to member.id,
                "username" to member.username
            )
        )
    }

    fun getPayload(token: String): Map<String, Any>? {
        if (!Ut.jwt.isValid(AppConfig.jwtSecretKey, token)) {
            return null
        }

        return Ut.jwt.getClaims(AppConfig.jwtSecretKey, token)
    }

    fun getMemberIdFromAccessToken(accessToken: String): Int {
        val payload = getPayload(accessToken) ?: return 0
        return (payload["id"] as Number).toInt()
    }
}
