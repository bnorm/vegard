package com.bnorm.vegard

import org.junit.jupiter.api.Assertions
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun assertTrue(condition: Boolean, message: String? = null) {
  contract {
    returns() implies condition
  }
  Assertions.assertTrue(condition, message)
}
