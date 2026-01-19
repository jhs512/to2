package com.back.domain.member.member.entity

import com.back.global.jpa.entity.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Transient
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

@Entity
class Member private constructor(
    id: Int,
    @Column(unique = true)
    val username: String,
    var password: String?,
    var nickname: String,
    @Column(unique = true)
    var apiKey: String,
    var profileImgUrl: String
) : BaseTime(id) {

    // 주 생성자 (JPA 및 일반 사용)
    constructor(
        username: String,
        password: String? = null,
        nickname: String,
        apiKey: String = UUID.randomUUID().toString(),
        profileImgUrl: String = ""
    ) : this(0, username, password, nickname, apiKey, profileImgUrl)

    // JWT payload에서 복원할 때 사용하는 보조 생성자
    constructor(id: Int, username: String, name: String) : this(
        id = id,
        username = username,
        password = null,
        nickname = name,
        apiKey = "",
        profileImgUrl = ""
    )

    val name: String
        get() = nickname.ifBlank { username }

    @get:Transient
    val authorities: Collection<GrantedAuthority>
        get() = listOf(SimpleGrantedAuthority("ROLE_USER"))

    fun modifyApiKey(apiKey: String) {
        this.apiKey = apiKey
    }
}
