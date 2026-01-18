package com.back.standard.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// 로거 확장
// 사용법: private val log = logger()
inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

// UrlSafe 한 Base64 인코딩
// 모든 문자열 객체에 추가
@OptIn(ExperimentalEncodingApi::class)
fun String.base64Encode(): String {
    return Base64.UrlSafe.encode(this.toByteArray(StandardCharsets.UTF_8))
}

// UrlSafe 한 Base64 디코딩
// 모든 문자열 객체에 추가
@OptIn(ExperimentalEncodingApi::class)
fun String.base64Decode(): String {
    return Base64.UrlSafe.decode(this).decodeToString()
}

fun String.toCamelCase() =
    this
        .split("_")
        .mapIndexed { index, word ->
            if (index == 0)
                word.lowercase()
            else
                word.lowercase().replaceFirstChar {
                    it.uppercase()
                }
        }.joinToString("")

// null이면 NoSuchElementException을 발생, null이 아니면 nullable 제거
// 모든 nullable 객체에 추가
fun <T : Any> T?.getOrThrow(): T {
    return this ?: throw NoSuchElementException()
}
