package com.bnorm.vegard.auth

import com.auth0.jwt.interfaces.Payload
import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.User
import com.bnorm.vegard.model.UserId
import io.ktor.auth.Principal

data class UserPrincipal(
  val user: User,
  val jwt: Payload
) : Principal

val Payload.userId
  get() = subject.takeIf { getClaim("role").asString() == "user" }
    ?.toLongOrNull()
    ?.let { UserId(it) }

val Payload.controllerId
  get() = subject.takeIf { getClaim("role").asString() == "controller" }
    ?.toLongOrNull()
    ?.let { ControllerId(it) }

val Payload.refresh
  get() = getClaim("refresh").asInt()
