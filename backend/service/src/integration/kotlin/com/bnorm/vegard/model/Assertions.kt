@file:Suppress("DuplicatedCode")

package com.bnorm.vegard.model

import com.bnorm.vegard.db.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.assertNotNull

fun assertUser(expected: UserPrototype?, actual: User?) {
  assertNotNull(expected)
  assertNotNull(actual)

  assertEquals(expected.email, actual.email, "email")
  assertEquals(expected.firstName, actual.firstName, "firstName")
  assertEquals(expected.lastName, actual.lastName, "lastName")
}

fun assertUser(expected: UserPrototype?, actual: UserEntity?) {
  assertNotNull(expected)
  assertNotNull(actual)

  assertEquals(expected.email, actual.email, "email")
  assertEquals(expected.firstName, actual.firstName, "firstName")
  assertEquals(expected.lastName, actual.lastName, "lastName")

  // Database should never store raw password
  assertNotEquals(expected.password, actual.password, "password")
}

fun assertUser(expected: User?, actual: UserEntity?) {
  assertNotNull(expected)
  assertNotNull(actual)

  assertEquals(expected.id.value, actual.id.value, "id")
  assertEquals(expected.email, actual.email, "email")
  assertEquals(expected.firstName, actual.firstName, "firstName")
  assertEquals(expected.lastName, actual.lastName, "lastName")
}
