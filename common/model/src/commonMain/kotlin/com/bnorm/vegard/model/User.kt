package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
  val id: UserId,
  val email: String,
  val firstName: String,
  val lastName: String
)
