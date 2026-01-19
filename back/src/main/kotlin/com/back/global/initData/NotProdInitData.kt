package com.back.global.initData

import com.back.domain.member.member.service.MemberService
import com.back.global.app.CustomConfigProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.Transactional

@Profile("!prod")
@Configuration
class NotProdInitData(
    private val memberService: MemberService,
    private val customConfigProperties: CustomConfigProperties,
) {
    @Lazy
    @Autowired
    private lateinit var self: NotProdInitData

    @Bean
    fun notProdInitDataApplicationRunner(): ApplicationRunner {
        return ApplicationRunner {
            self.work1()
        }
    }

    @Transactional
    fun work1() {
        if (memberService.count() > 0) return

        val memberSystem = memberService.join("system", "1234", "시스템", "")
        memberSystem.modifyApiKey(memberSystem.username)

        val memberAdmin = memberService.join("admin", "1234", "관리자", "")
        memberAdmin.modifyApiKey(memberAdmin.username)

        val memberUser1 = memberService.join("user1", "1234", "유저1", "")
        memberUser1.modifyApiKey(memberUser1.username)

        val memberUser2 = memberService.join("user2", "1234", "유저2", "")
        memberUser2.modifyApiKey(memberUser2.username)

        // 설정 파일에서 추가 멤버 생성
        customConfigProperties.notProdMembers.forEach { notProdMember ->
            val member = memberService.modifyOrJoin(
                notProdMember.username,
                notProdMember.nickname,
                notProdMember.profileImgUrl
            )
            member.modifyApiKey(notProdMember.apiKey)
        }
    }
}
