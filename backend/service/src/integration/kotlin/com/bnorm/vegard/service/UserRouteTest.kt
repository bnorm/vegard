package com.bnorm.vegard.service

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@Suppress("FunctionName")
class UserRouteTest : ServiceBaseTest() {
  @Test
  fun `user can be created`() = withApp { userRobot ->
    userRobot.createUser(
      email = "new_user@example",
      password = "bad_password",
      firstName = "New",
      lastName = "User"
    ).success()
  }

  @Test
  fun `user cannot be created without email`() = withApp { userRobot ->
    userRobot.createUser(
      password = "bad_password",
      firstName = "New",
      lastName = "User"
    ).badRequest()
  }

  @Test
  fun `user cannot be created without password`() = withApp { userRobot ->
    userRobot.createUser(
      email = "new_user@example",
      firstName = "New",
      lastName = "User"
    ).badRequest()
  }

  @Test
  fun `user cannot be created without first name`() = withApp { userRobot ->
    userRobot.createUser(
      email = "new_user@example",
      password = "bad_password",
      lastName = "User"
    ).badRequest()
  }

  @Test
  fun `user cannot be created without last name`() = withApp { userRobot ->
    userRobot.createUser(
      email = "new_user@example",
      password = "bad_password",
      firstName = "New"
    ).badRequest()
  }

  @Test
  fun `user can be retrieved`() = withApp { userRobot ->
    val created = userRobot.createUser(
      email = "new_user@example",
      password = "bad_password",
      firstName = "New",
      lastName = "User"
    ).success()

    val retrieved = userRobot.getUser(created.id).success()
    assertEquals(created, retrieved)
  }

  @Test
  fun `user can only be retrieved by integer`() = withApp { userRobot ->
    userRobot.getUser("user_id_1").badRequest()
  }
}
