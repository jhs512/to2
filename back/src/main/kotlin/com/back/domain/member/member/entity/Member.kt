package com.back.domain.member.member.entity

import com.back.global.jpa.entity.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.util.*

@Entity
class Member(
    @Column(unique = true)
    val username: String,
    var password: String? = null,
    var nickname: String,
    @Column(unique = true)
    var apiKey: String = UUID.randomUUID().toString(),
    var profileImgUrl: String = ""
) : BaseTime() {

    val name: String
        get() = nickname.ifBlank { username }
}
