package com.bnorm.vegard.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.bnorm.vegard.model.UserId
import java.time.Duration
import java.time.Instant
import java.util.Date
import javax.inject.Inject

class JwtService(
  private val algorithm: Algorithm,
  private val issuer: String = "vegard.bnorm.com",
  private val validityDuration: Duration = Duration.ofMinutes(30)
) {
  @Inject
  constructor(algorithm: Algorithm) : this(algorithm, "vegard.bnorm.com", Duration.ofMinutes(30))

  val verifier: JWTVerifier = JWT.require(algorithm)
    .withIssuer(issuer)
    .build()

  fun createToken(userId: UserId): String = JWT.create()
    .withSubject(userId.value.toString())
    .withIssuer(issuer)
    .withExpiresAt(Date.from(Instant.now() + validityDuration))
    .sign(algorithm)
}
