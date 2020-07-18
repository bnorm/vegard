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
      service.userVerifier.verify(token)
    }
  }

  @ParameterizedTest(name = "Known good token can be verified")
  @CsvSource(
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJyb2xlIjoidXNlciIsImV4cCI6OTIyMzM3MjAzNjg1NH0.3_aEc_ILrNnwLQgPpIHe8TfUy3xqAEOumOy6JwJYAf7s-maEEl65W2N4XXtAUz-B9ardxQE8kBlzNHr3u3X6og"
  )
  fun `known good tokens can be verified`(token: String) {
    assertDoesNotThrow {
      service.userVerifier.verify(token)
    }
  }

  @ParameterizedTest(name = "Known bad token fails verification because: {1}")
  @CsvSource(
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJyb2xlIjoidXNlciIsImV4cCI6MH0.sH3h_DLoqArJsScit9pW9zTHPHq1L7kgObH_t7uZBUw85QH4NJgprj55ac2rEBHW6O5Ja6fkGpL79Y041HuV0A," +
      "The Token has expired on Thu Jan 01 00:00:00 UTC 1970.",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoibm90LWp1bml0Iiwicm9sZSI6InVzZXIiLCJleHAiOjkyMjMzNzIwMzY4NTR9.Z8qKwAC95rzS2CxUt3pJ7fsQae6RTeBqk3BXHPictFOTtFGaKptdKosPZWhIBnhCdZFHDX1rHIuyL0aJypBxRA," +
      "The Claim 'iss' value doesn't match the required issuer.",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJyb2xlIjoidXNlciIsImV4cCI6OTIyMzM3MjAzNjg1NH0.AVeu225uDFcDdkI2vWvYI5JkVKyWbuppy3I4iipd32npbqnP-ns-KUWkYDpOsungR9I0x2B1i7PN99Lr3gnw5g," +
      "The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA512"
  )
  fun `known bad tokens fail verification`(token: String, message: String) {
    val exception = assertThrows<JWTVerificationException> {
      service.userVerifier.verify(token)
    }
    assertEquals(message, exception.message)
  }
}
