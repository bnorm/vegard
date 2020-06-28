package com.bnorm.vegard.service

import com.bnorm.vegard.model.Password
import com.bnorm.vegard.model.UserLoginRequest
import com.bnorm.vegard.model.UserPrototype
import com.bnorm.vegard.model.assertUser
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

class UserServiceTest : ServiceBaseTest() {
  private val expected = UserPrototype("jane@example.com", Password("vegard"), "Jane", "Doe")

  @Test
  fun `users can be created`() = runBlocking {
    // TODO: introduce test robots!
    val created = userService.createUser(expected)
    assertUser(expected, created)

    val actual = userRepository.findUserByEmail(created.email)
    assertUser(expected, actual)
    assertUser(created, actual)
  }

  @Test
  fun `users can be retrieved by id`() = runBlocking {
    // TODO: introduce test robots!
    val created = userService.createUser(expected)

    val actual = userService.findUserById(created.id)
    assertUser(expected, actual)
  }

  @Test
  fun `users can be authenticated`() = runBlocking {
    // TODO: introduce test robots!
    userService.createUser(expected)

    val actual = userService.findUserByCredentials(UserLoginRequest(expected.email, expected.password))
    assertUser(expected, actual)
  }

  @Test
  fun `unknown users can not be authenticated`() = runBlocking {
    val actual = userService.findUserByCredentials(UserLoginRequest("unknown", Password("password")))
    assertNull(actual)
  }

  @Test
  fun `users cannot be authenticated with incorrect password`() = runBlocking {
    // TODO: introduce test robots!
    userService.createUser(expected)

    val actual = userService.findUserByCredentials(UserLoginRequest("unknown", Password("badpassword")))
    assertNull(actual)
  }
}
