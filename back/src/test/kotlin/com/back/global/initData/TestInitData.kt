package com.back.global.initData

import com.back.domain.member.member.service.MemberService
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.Transactional

@Profile("test")
@Configuration
class TestInitData(
    private val memberService: MemberService
) {
    @Bean
    @Transactional
    fun testInitDataApplicationRunner(): ApplicationRunner {
        return ApplicationRunner {
            // 테스트용 사용자 생성
            memberService.modifyOrJoin(
                username = "user1",
                nickname = "유저1",
                profileImgUrl = "https://example.com/user1.png"
            )

            memberService.modifyOrJoin(
                username = "user2",
                nickname = "유저2",
                profileImgUrl = "https://example.com/user2.png"
            )
        }
    }
}
