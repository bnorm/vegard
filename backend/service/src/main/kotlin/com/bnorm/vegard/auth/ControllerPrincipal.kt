package com.bnorm.vegard.auth

import com.auth0.jwt.interfaces.Payload
import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.model.User
import io.ktor.auth.Principal

data class ControllerPrincipal(
  val controller: Controller,
  val jwt: Payload
) : Principal
