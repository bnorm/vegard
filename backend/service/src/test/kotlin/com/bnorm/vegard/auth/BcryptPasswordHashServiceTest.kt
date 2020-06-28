package com.bnorm.vegard.auth

import com.bnorm.vegard.assertTrue
import com.bnorm.vegard.model.Password
import com.bnorm.vegard.model.PasswordHash
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class BcryptPasswordHashServiceTest {
  private val service = BcryptPasswordHashService()

  @Test
  fun `hash of password can be verified`() {
    val password = Password("Password1!")
    val hash = service.hash(password)
    assertTrue(service.verify(password, hash))
  }

  @ParameterizedTest
  @CsvSource(value = [
    "Password1!, \$2a\$12\$U0iBfYhG0bTMUnPpZU7qRuJyBRYKRGI4EB7u0sHbHPiFOH5jtUFt2"
  ])
  fun `known hash can be verified`(knownPassword: Password, knownHash: PasswordHash) {
    assertTrue(service.verify(knownPassword, knownHash))
  }

  @ParameterizedTest
  @CsvSource(value = [
    "P@ssw0rd, \$2a\$12\$U0iBfYhG0bTMUnPpZU7qRuJyBRYKRGI4EB7u0sHbHPiFOH5jtUFt2"
  ])
  fun `incorrect password fails verification`(incorrectPassword: Password, knownHash: PasswordHash) {
    assertTrue(!service.verify(incorrectPassword, knownHash))
  }

  @Test
  fun `long passwords are acceptable`() {
    val password = Password(buildString { repeat(128) { append('a') } })
    val hash = service.hash(password)
    assertTrue(service.verify(password, hash))
  }
}
