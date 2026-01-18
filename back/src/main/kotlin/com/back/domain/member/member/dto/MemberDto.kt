package com.back.domain.member.member.dto

import com.back.domain.member.member.entity.Member
import java.time.LocalDateTime

data class MemberDto(
    val id: Int,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val username: String,
    val nickname: String,
    val profileImgUrl: String
) {
    companion object {
        fun from(member: Member): MemberDto {
            return MemberDto(
                id = member.id,
                createDate = member.createDate,
                modifyDate = member.modifyDate,
                username = member.username,
                nickname = member.nickname,
                profileImgUrl = member.profileImgUrl
            )
        }
    }
}
