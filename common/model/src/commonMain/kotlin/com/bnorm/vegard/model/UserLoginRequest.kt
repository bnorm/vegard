package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
  val email: String,
  val password: Password
)
