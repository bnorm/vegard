package com.bnorm.vegard.auth

import com.auth0.jwt.interfaces.Payload
import com.bnorm.vegard.model.User
import io.ktor.auth.Principal

data class UserPrincipal(
  val user: User,
  val jwt: Payload
) : Principal
