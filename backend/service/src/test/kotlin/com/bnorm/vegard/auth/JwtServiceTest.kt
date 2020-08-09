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
    val decoded = service.userVerifier.verify(token)
    assertEquals(UserId(1), decoded.userId)
  }

  @ParameterizedTest(name = "Known good token can be verified")
  @CsvSource(
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJyb2xlIjoidXNlciIsInJlZnJlc2giOjAsImV4cCI6OTIyMzM3MjAzNjg1NH0.Amk2qteqhlijmYhNGi_44ENVtAc-Xx2Zuoql4-eWV2X4y2uO0AQX-qoSToNCqjoufdnDM050EWgDFkGsVUkiAw"
  )
  fun `known good tokens can be verified`(token: String) {
    assertDoesNotThrow {
      service.userVerifier.verify(token)
    }
  }

  @ParameterizedTest(name = "Known bad token fails verification because: {1}")
  @CsvSource(
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJyb2xlIjoidXNlciIsInJlZnJlc2giOjAsImV4cCI6MH0.aTVMDyvKheFrUUrwyeL7hmR4K5Fuqt5hnoPOurrzgoDwGA87ULDkzMF21SLX9iHsUtEQpFVpVD4DIjO2ANYabw," +
      "The Token has expired on Thu Jan 01 00:00:00 UTC 1970.",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoibm90LWp1bml0Iiwicm9sZSI6InVzZXIiLCJyZWZyZXNoIjowLCJleHAiOjkyMjMzNzIwMzY4NTR9.BhzHFfKjGYMZ_w_rl-PbVMRzQQaQGVC7IP-nNpjQd3dhh0aujCoA-ltql9jwY6zrkxSMhL9rQ1TQ4MWPslDufw," +
      "The Claim 'iss' value doesn't match the required issuer.",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoianVuaXQiLCJyb2xlIjoidXNlciIsInJlZnJlc2giOjAsImV4cCI6OTIyMzM3MjAzNjg1NH0.5TsmZj2WhnYAJMIAMbPK-ErKHO9CAtXJmQdHYr708iLywSu9md60ibFUlSzt2lYgjOhE4JBIF-x3QDsaD8Vy0w," +
      "The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA512"
  )
  fun `known bad tokens fail verification`(token: String, message: String) {
    val exception = assertThrows<JWTVerificationException> {
      service.userVerifier.verify(token)
    }
    assertEquals(message, exception.message)
  }
}
