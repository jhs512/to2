package com.back.domain.member.member.controller

import com.back.domain.member.member.dto.MemberDto
import com.back.global.exceptions.DomainException
import com.back.global.rq.Rq
import com.back.global.rsData.RsData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "ApiV1MemberController", description = "회원 API")
class ApiV1MemberController(
    private val rq: Rq
) {

    @GetMapping("/me")
    @Transactional(readOnly = true)
    @Operation(summary = "내 정보")
    fun me(): RsData<MemberDto> {
        val actor = rq.actorOrNull ?: throw DomainException("401-1", "로그인이 필요합니다.")
        return RsData("200-1", "내 정보 조회 성공", MemberDto.from(actor))
    }

    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃")
    fun logout(): RsData<Unit> {
        rq.deleteCookie("accessToken")
        rq.deleteCookie("apiKey")
        return RsData("200-1", "로그아웃 성공")
    }
}
