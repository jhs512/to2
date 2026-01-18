package com.back.global.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class CustomOAuth2AuthorizationRequestResolver(
    clientRegistrationRepository: ClientRegistrationRepository,
    authorizationRequestBaseUri: String
) : OAuth2AuthorizationRequestResolver {

    private val defaultResolver = DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository,
        authorizationRequestBaseUri
    )

    override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        val authorizationRequest = defaultResolver.resolve(request)
        return customizeAuthorizationRequest(request, authorizationRequest)
    }

    override fun resolve(request: HttpServletRequest, clientRegistrationId: String): OAuth2AuthorizationRequest? {
        val authorizationRequest = defaultResolver.resolve(request, clientRegistrationId)
        return customizeAuthorizationRequest(request, authorizationRequest)
    }

    private fun customizeAuthorizationRequest(
        request: HttpServletRequest,
        authorizationRequest: OAuth2AuthorizationRequest?
    ): OAuth2AuthorizationRequest? {
        if (authorizationRequest == null) {
            return null
        }

        val redirectUrl = request.getParameter("redirectUrl")

        val customState = buildString {
            append("state=")
            append(authorizationRequest.state ?: "")
            if (!redirectUrl.isNullOrBlank()) {
                append("&redirectUrl=")
                append(URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8))
            }
        }

        return OAuth2AuthorizationRequest.from(authorizationRequest)
            .state(URLEncoder.encode(customState, StandardCharsets.UTF_8))
            .build()
    }
}
