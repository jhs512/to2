package com.back.global.security

import com.back.domain.member.member.service.AuthTokenService
import com.back.domain.member.member.service.MemberService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val memberService: MemberService,
    private val authTokenService: AuthTokenService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = extractToken(request, "accessToken")
        val apiKey = extractToken(request, "apiKey")

        if (!accessToken.isNullOrBlank()) {
            authenticateWithAccessToken(accessToken)
        } else if (!apiKey.isNullOrBlank()) {
            authenticateWithApiKey(apiKey)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest, name: String): String? {
        // First try to get from cookie
        val cookieValue = request.cookies?.find { it.name == name }?.value
        if (!cookieValue.isNullOrBlank()) {
            return cookieValue
        }

        // Then try to get from header
        val headerValue = request.getHeader(name)
        if (!headerValue.isNullOrBlank()) {
            return headerValue
        }

        // For accessToken, also check Authorization header
        if (name == "accessToken") {
            val authHeader = request.getHeader("Authorization")
            if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7)
            }
        }

        return null
    }

    private fun authenticateWithAccessToken(accessToken: String) {
        val memberId = authTokenService.getMemberIdFromAccessToken(accessToken)
        if (memberId == 0) {
            return
        }

        val member = memberService.findById(memberId).orElse(null) ?: return

        val securityUser = SecurityUser.from(member)

        val authentication = UsernamePasswordAuthenticationToken(
            securityUser,
            null,
            securityUser.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun authenticateWithApiKey(apiKey: String) {
        val member = memberService.findByApiKey(apiKey).orElse(null) ?: return

        val securityUser = SecurityUser.from(member)

        val authentication = UsernamePasswordAuthenticationToken(
            securityUser,
            null,
            securityUser.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication
    }
}
