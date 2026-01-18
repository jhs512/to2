package com.back.global.security

import com.back.domain.member.member.service.MemberService
import com.back.global.app.AppConfig
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Component
class CustomOAuth2LoginSuccessHandler(
    private val memberService: MemberService
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val securityUser = authentication.principal as SecurityUser

        val member = memberService.findById(securityUser.id).orElseThrow()

        val accessToken = memberService.genAccessToken(member)

        addCookie(response, "accessToken", accessToken, AppConfig.accessTokenExpirationSeconds.toInt())
        addCookie(response, "apiKey", member.apiKey, -1)

        val redirectUrl = extractRedirectUrl(request)

        response.sendRedirect(redirectUrl)
    }

    private fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.domain = AppConfig.siteCookieDomain
        cookie.isHttpOnly = true
        cookie.secure = AppConfig.siteFrontUrl.startsWith("https")
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    private fun extractRedirectUrl(request: HttpServletRequest): String {
        val state = request.getParameter("state")
        if (state != null) {
            try {
                val decodedState = URLDecoder.decode(state, StandardCharsets.UTF_8)
                val params = decodedState.split("&").associate {
                    val (key, value) = it.split("=", limit = 2)
                    key to value
                }
                val redirectUrl = params["redirectUrl"]
                if (!redirectUrl.isNullOrBlank()) {
                    return URLDecoder.decode(redirectUrl, StandardCharsets.UTF_8)
                }
            } catch (e: Exception) {
                // ignore parsing errors
            }
        }
        return AppConfig.siteFrontUrl
    }
}
