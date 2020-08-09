package com.bnorm.vegard.inject

import com.auth0.jwt.algorithms.Algorithm
import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.ktor.application.Application
import java.time.Duration
import javax.inject.Qualifier

@Qualifier
annotation class JwtDuration

@Qualifier
annotation class JwtIssuer

class SecurityModule : AbstractModule() {

  @Provides
  fun provideJwtAlgorithm(application: Application): Algorithm {
    return Algorithm.HMAC512(application.environment.config.property("jwt.secret").getString())
  }

  @Provides
  @JwtIssuer
  fun provideJwtIssuer(application: Application): String {
    return "com.bnorm.vegard"
  }

  @Provides
  @JwtDuration
  fun provideJwtDuration(application: Application): Duration {
    return Duration.ofHours(48)
  }
}
