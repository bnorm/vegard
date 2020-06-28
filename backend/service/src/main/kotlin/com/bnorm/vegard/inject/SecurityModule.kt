package com.bnorm.vegard.inject

import com.auth0.jwt.algorithms.Algorithm
import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.ktor.application.Application

class SecurityModule : AbstractModule() {

  @Provides
  fun provideJwtAlgorithm(application: Application): Algorithm {
    return Algorithm.HMAC512(application.environment.config.property("jwt.secret").getString())
  }
}
