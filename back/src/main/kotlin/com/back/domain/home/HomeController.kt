package com.back.domain.home

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "HomeController", description = "홈 컨트롤러")
class HomeController {

    @GetMapping("/")
    @Operation(summary = "메인 페이지")
    fun main(): String {
        return "Hello, World!"
    }
}
