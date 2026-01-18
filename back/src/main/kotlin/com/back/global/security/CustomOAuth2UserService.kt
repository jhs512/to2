package com.back.global.security

import com.back.domain.member.member.service.MemberService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomOAuth2UserService(
    private val memberService: MemberService
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val attributes = oAuth2User.attributes

        val (username, nickname, profileImgUrl) = when (registrationId) {
            "google" -> extractGoogleUserInfo(attributes)
            else -> throw IllegalArgumentException("Unsupported OAuth2 provider: $registrationId")
        }

        val member = memberService.modifyOrJoin(username, nickname, profileImgUrl)

        return SecurityUser.from(member)
    }

    private fun extractGoogleUserInfo(attributes: Map<String, Any>): Triple<String, String, String> {
        val googleId = attributes["sub"] as String
        val username = "GOOGLE__$googleId"
        val nickname = attributes["name"] as? String ?: ""
        val profileImgUrl = attributes["picture"] as? String ?: ""

        return Triple(username, nickname, profileImgUrl)
    }
}
