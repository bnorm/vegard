package com.bnorm.vegard

import com.bnorm.vegard.auth.PasswordHashService
import com.bnorm.vegard.db.UserRepository
import com.bnorm.vegard.inject.ApplicationModule
import com.bnorm.vegard.inject.DatabaseModule
import com.bnorm.vegard.inject.SecurityModule
import com.bnorm.vegard.model.Password
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import io.ktor.application.Application
import io.ktor.server.netty.EngineMain
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway

fun main(args: Array<String>) {
  EngineMain.main(args)
}

@Suppress("unused") // used by application.conf
fun Application.main() {
  val injector = Guice.createInjector(
    ApplicationModule(this),
    DatabaseModule(),
    SecurityModule()
  )

  injector.getInstance<Flyway>().migrate()

  val userRepository = injector.getInstance<UserRepository>()
  val passwordHashService = injector.getInstance<PasswordHashService>()
  runBlocking {
    if (userRepository.findUserByEmail("brian@bnorm.com") == null) {
      userRepository.createUser("brian@bnorm.com", passwordHashService.hash(Password("vegard")), "Brian", "Norman")
    }
  }

  app(injector.getInstance(), injector.getInstance(), injector.getInstance())
}

inline fun <reified T> Injector.getInstance(): T {
  return getInstance(object : Key<T>() {})
}
