package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPrototype(
  val email: String,
  val password: Password,
  val firstName: String,
  val lastName: String
)
