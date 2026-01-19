package com.back.domain.member.member.controller

import com.back.domain.member.member.service.MemberService
import com.back.standard.extensions.getOrThrow
import jakarta.servlet.http.Cookie
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiV1MemberControllerTest {
    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    @DisplayName("내 정보, with apiKey Cookie")
    fun t1() {
        val actor = memberService.findByUsername("user1").getOrThrow()

        val resultActions = mvc
            .perform(
                get("/api/v1/members/me")
                    .cookie(Cookie("apiKey", actor.apiKey))
            )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(ApiV1MemberController::class.java))
            .andExpect(handler().methodName("me"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(actor.id))
            .andExpect(jsonPath("$.createDate").value(Matchers.startsWith(actor.createDate.toString().take(20))))
            .andExpect(jsonPath("$.modifyDate").value(Matchers.startsWith(actor.modifyDate.toString().take(20))))
            .andExpect(jsonPath("$.name").value(actor.name))
            .andExpect(jsonPath("$.username").value(actor.username))
    }

    @Test
    @DisplayName("내 정보, 인증 없이 접근 시 실패")
    fun t2() {
        val resultActions = mvc
            .perform(
                get("/api/v1/members/me")
            )
            .andDo(print())

        // OAuth2 로그인 설정으로 인해 302 리다이렉트 반환
        resultActions
            .andExpect(status().is3xxRedirection)
    }

    @Test
    @DisplayName("로그아웃")
    fun t3() {
        val resultActions = mvc
            .perform(
                delete("/api/v1/members/auth")
            )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(ApiV1MemberController::class.java))
            .andExpect(handler().methodName("logout"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("로그아웃 되었습니다."))
            .andExpect { result ->
                val apiKeyCookie = result.response.getCookie("apiKey")
                assertThat(apiKeyCookie).isNotNull
                assertThat(apiKeyCookie!!.value).isEmpty()
                assertThat(apiKeyCookie.maxAge).isEqualTo(0)
                assertThat(apiKeyCookie.path).isEqualTo("/")
                assertThat(apiKeyCookie.isHttpOnly).isTrue

                val accessTokenCookie = result.response.getCookie("accessToken")
                assertThat(accessTokenCookie).isNotNull
                assertThat(accessTokenCookie!!.value).isEmpty()
                assertThat(accessTokenCookie.maxAge).isEqualTo(0)
                assertThat(accessTokenCookie.path).isEqualTo("/")
                assertThat(accessTokenCookie.isHttpOnly).isTrue
            }
    }

    @Test
    @DisplayName("내 정보, with accessToken Cookie")
    fun t4() {
        val actor = memberService.findByUsername("user1").getOrThrow()
        val accessToken = memberService.genAccessToken(actor)

        val resultActions = mvc
            .perform(
                get("/api/v1/members/me")
                    .cookie(Cookie("accessToken", accessToken))
                    .cookie(Cookie("apiKey", actor.apiKey))
            )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(ApiV1MemberController::class.java))
            .andExpect(handler().methodName("me"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(actor.id))
            .andExpect(jsonPath("$.name").value(actor.name))
    }
}
