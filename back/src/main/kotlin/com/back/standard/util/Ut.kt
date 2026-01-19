package com.back.standard.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import tools.jackson.databind.ObjectMapper
import java.util.*
import javax.crypto.SecretKey

object Ut {
    object JSON {
        lateinit var objectMapper: ObjectMapper

        fun toString(obj: Any, defaultValue: String = ""): String {
            return try {
                objectMapper.writeValueAsString(obj)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    object cmd {
        fun run(vararg args: String) {
            val isWindows = System
                .getProperty("os.name")
                .lowercase(Locale.getDefault())
                .contains("win")

            val builder = ProcessBuilder(
                args
                    .map { it.replace("{{DOT_CMD}}", if (isWindows) ".cmd" else "") }
                    .toList()
            )

            builder.redirectErrorStream(true)

            val process = builder.start()

            process.inputStream.bufferedReader().useLines { lines ->
                lines.forEach { println(it) }
            }

            val exitCode = process.waitFor()

            println("종료 코드: $exitCode")
        }

        fun runAsync(vararg args: String) {
            Thread(Runnable {
                run(*args)
            }).start()
        }
    }

    object JWT {
        fun createToken(secretKeyStr: String, expireSeconds: Long, claims: Map<String, Any>): String {
            val secretKey = Keys.hmacShaKeyFor(
                Base64.getEncoder().encodeToString(secretKeyStr.toByteArray()).toByteArray()
            )

            val issuedAt = Date()
            val expiration = Date(issuedAt.time + 1000L * expireSeconds)

            return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact()
        }

        fun isValid(secretKeyStr: String, token: String): Boolean {
            val secretKey = Keys.hmacShaKeyFor(
                Base64.getEncoder().encodeToString(secretKeyStr.toByteArray()).toByteArray()
            )

            return try {
                Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                true
            } catch (e: Exception) {
                false
            }
        }

        fun getClaims(secretKeyStr: String, token: String): Map<String, Any> {
            val secretKey: SecretKey = Keys.hmacShaKeyFor(
                Base64.getEncoder().encodeToString(secretKeyStr.toByteArray()).toByteArray()
            )

            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        }

        fun payload(secret: String, jwtStr: String): Map<String, Any>? {
            return try {
                val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

                Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(jwtStr)
                    .payload as Map<String, Any>
            } catch (e: Exception) {
                null
            }
        }
    }

    object STR {
        fun isBlank(str: String?): Boolean {
            return str.isNullOrBlank()
        }

        fun isNotBlank(str: String?): Boolean {
            return !isBlank(str)
        }
    }
}
