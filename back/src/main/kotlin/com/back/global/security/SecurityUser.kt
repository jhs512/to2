package com.back.global.security

import com.back.domain.member.member.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class SecurityUser(
    val id: Int,
    private val _username: String,
    private val _password: String,
    private val authorities: Collection<GrantedAuthority>,
    private val attributes: Map<String, Any> = emptyMap()
) : UserDetails, OAuth2User {

    companion object {
        fun from(member: Member): SecurityUser {
            return SecurityUser(
                id = member.id,
                _username = member.username,
                _password = member.password ?: "",
                authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
            )
        }
    }

    override fun getUsername(): String = _username

    override fun getPassword(): String = _password

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun getName(): String = _username

    override fun getAttributes(): Map<String, Any> = attributes
}
