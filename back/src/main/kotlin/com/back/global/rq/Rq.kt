package com.back.global.rq

import com.back.domain.member.member.entity.Member
import com.back.domain.member.member.service.MemberService
import com.back.global.app.AppConfig
import com.back.global.security.SecurityUser
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class Rq(
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val memberService: MemberService
) {
    private var _actor: Member? = null
    private var actorResolved = false

    val actor: Member?
        get() {
            if (!actorResolved) {
                _actor = resolveActor()
                actorResolved = true
            }
            return _actor
        }

    private fun resolveActor(): Member? {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: return null

        val principal = authentication.principal
        if (principal is SecurityUser) {
            return memberService.findById(principal.id).orElse(null)
        }
        return null
    }

    fun getCookie(name: String): String? {
        return request.cookies?.find { it.name == name }?.value
    }

    fun setCookie(name: String, value: String, maxAge: Int = -1) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.domain = AppConfig.siteCookieDomain
        cookie.isHttpOnly = true
        cookie.secure = AppConfig.siteFrontUrl.startsWith("https")
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    fun deleteCookie(name: String) {
        val cookie = Cookie(name, "")
        cookie.path = "/"
        cookie.domain = AppConfig.siteCookieDomain
        cookie.maxAge = 0
        response.addCookie(cookie)
    }

    fun redirect(url: String) {
        response.sendRedirect(url)
    }
}
