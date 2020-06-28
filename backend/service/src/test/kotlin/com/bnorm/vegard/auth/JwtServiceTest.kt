package com.bnorm.vegard.auth

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.bnorm.vegard.model.UserId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.TimeZone

class JwtServiceTest {
  init {
    // Set default time zone to UTC for date string consistency
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
  }

  private val service = JwtService(
    algorithm = Algorithm.HMAC512("secret"), // hardcode a weak secret for testing
    issuer = "junit" // use a custom issuer for testing
  )

  @Test
  fun `created token can be verified`() {
    val token = service.createToken(UserId(1))
    assertDoesNotThrow {
      service.verifier.verify(token)
    }
  }

  @ParameterizedTest(name = "Known good token can be verified")
  @CsvSource(
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJleHAiOjkyMjMzNzIwMzY4NTQ3NzV9.H4A5b-6C7aWmQ_RnC4OxwYVGQ-9KMJWTlikWT1zlVIhvaMG2djE2nFqAGpGGlT7YUc80AcEZ8Sbv7FJeOIUGag"
  )
  fun `known good tokens can be verified`(token: String) {
    assertDoesNotThrow {
      service.verifier.verify(token)
    }
  }

  @ParameterizedTest(name = "Known bad token fails verification because: {1}")
  @CsvSource(
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJleHAiOjB9.GFs2_79z1YDB0kY8mC_tmFTdEP9ySAu3it9Xq-PfBfWQQVllBikhqsgSOYjnPlxESUX52w5hlehd5dMS5LVvHg," +
      "The Token has expired on Thu Jan 01 00:00:00 UTC 1970.",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoibm90LWp1bml0IiwiZXhwIjo5MjIzMzcyMDM2ODU0Nzc2fQ.a8eN_82Aj67bOV1z_FbSJE3npns5vkoXeY2ieVRVV73efNQbgTPcdLnNOEZYHaIZBv-C49HdTP3BSmYDEwg0OQ," +
      "The Claim 'iss' value doesn't match the required issuer.",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJleHAiOjkyMjMzNzIwMzY4NTQ3NzZ9.vair_m3yFw6gNPL7ULH1PXgdwLv0Oo3QZ-yMU61PMyzbjdFX-zUhYW6tknp1LKgO08kJJft7Xu0tNcNvPsW6bA," +
      "The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA512"
  )
  fun `known bad tokens fail verification`(token: String, message: String) {
    val exception = assertThrows<JWTVerificationException> {
      service.verifier.verify(token)
    }
    assertEquals(message, exception.message)
  }
}
