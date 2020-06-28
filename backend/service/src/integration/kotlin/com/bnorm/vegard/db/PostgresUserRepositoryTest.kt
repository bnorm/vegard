package com.bnorm.vegard.db

import com.bnorm.vegard.assertTrue
import com.bnorm.vegard.model.PasswordHash
import com.bnorm.vegard.model.UserId
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@Suppress("FunctionName")
class PostgresUserRepositoryTest : DatabaseBaseTest() {
  @Test
  fun `created users can be retrieved`() = runBlocking {
    val repository = PostgresUserRepository(database)
    val expected = repository.createUser("brian@bnorm.com", PasswordHash("temp"), "Brian", "Norman")
    val actual = repository.findUserById(UserId(expected.id.value))
    assertTrue(actual != null &&
      actual.id == expected.id &&
      actual.email == expected.email &&
      actual.password == PasswordHash("temp") &&
      actual.firstName == expected.firstName &&
      actual.lastName == expected.lastName)
  }
}
