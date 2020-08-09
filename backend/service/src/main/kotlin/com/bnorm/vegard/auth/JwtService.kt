package com.bnorm.vegard.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.bnorm.vegard.inject.JwtDuration
import com.bnorm.vegard.inject.JwtIssuer
import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.UserId
import java.time.Duration
import java.time.Instant
import java.util.Date
import javax.inject.Inject

class JwtService @Inject constructor(
  private val algorithm: Algorithm,
  @JwtIssuer private val issuer: String = "vegard.bnorm.com",
  @JwtDuration private val validityDuration: Duration = Duration.ofMinutes(30)
) {
  val userVerifier: JWTVerifier = JWT.require(algorithm)
    .withIssuer(issuer)
    .withClaim("role", "user")
    .build()

  fun createToken(userId: UserId, refreshesRemaining: Int? = null): String = JWT.create()
    .withSubject(userId.value.toString())
    .withClaim("role", "user")
    .withClaim("refresh", refreshesRemaining ?: 0)
    .withIssuer(issuer)
    .withExpiresAt(Date.from(Instant.now() + validityDuration))
    .sign(algorithm)

  val controllerVerifier: JWTVerifier = JWT.require(algorithm)
    .withIssuer(issuer)
    .withClaim("role", "controller")
    .build()

  fun createToken(controllerId: ControllerId): String = JWT.create()
    .withSubject(controllerId.value.toString())
    .withClaim("role", "controller")
    .withIssuer(issuer)
    .withExpiresAt(Date.from(Instant.now() + validityDuration))
    .sign(algorithm)
}
