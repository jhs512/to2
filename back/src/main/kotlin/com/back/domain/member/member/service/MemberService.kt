package com.back.domain.member.member.service

import com.back.domain.member.member.entity.Member
import com.back.domain.member.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val authTokenService: AuthTokenService,
    private val paswordEncodder: PasswordEncoder
) {
    fun count(): Long = memberRepository.count()

    fun findByUsername(username: String): Member? {
        return memberRepository.findByUsername(username)
    }

    fun findByApiKey(apiKey: String): Member? {
        return memberRepository.findByApiKey(apiKey)
    }

    fun findById(id: Int): Member? {
        return memberRepository.findById(id).orElse(null)
    }

    fun join(username: String, password: String, nickname: String, profileImgUrl: String): Member {
        val newMember = Member(
            username = username,
            password = password,
            nickname = nickname,
            profileImgUrl = profileImgUrl
        )

        return memberRepository.save(newMember)
    }

    fun modifyOrJoin(username: String, nickname: String, profileImgUrl: String): Member {
        val member = findByUsername(username)

        if (member != null) {
            member.nickname = nickname
            if (profileImgUrl.isNotBlank()) {
                member.profileImgUrl = profileImgUrl
            }
            return member
        }

        val newMember = Member(
            username = username,
            nickname = nickname,
            profileImgUrl = profileImgUrl
        )

        return memberRepository.save(newMember)
    }

    fun genAccessToken(member: Member): String {
        return authTokenService.genAccessToken(member)
    }

    fun payload(accessToken: String): Map<String, Any>? {
        return authTokenService.payload(accessToken)
    }
}
