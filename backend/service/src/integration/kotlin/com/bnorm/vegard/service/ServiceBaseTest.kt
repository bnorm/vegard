package com.bnorm.vegard.service

import com.auth0.jwt.algorithms.Algorithm
import com.bnorm.vegard.app
import com.bnorm.vegard.auth.BcryptPasswordHashService
import com.bnorm.vegard.auth.JwtService
import com.bnorm.vegard.db.ControllerRepository
import com.bnorm.vegard.db.DatabaseBaseTest
import com.bnorm.vegard.db.PostgresControllerRepository
import com.bnorm.vegard.db.PostgresUserRepository
import com.bnorm.vegard.db.UserRepository
import com.bnorm.vegard.model.Password
import com.bnorm.vegard.model.User
import com.bnorm.vegard.model.UserPrototype
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach

abstract class ServiceBaseTest : DatabaseBaseTest() {

  protected val jwtConfig = JwtService(algorithm = Algorithm.HMAC512("junit"))
  protected lateinit var userRepository: UserRepository
  protected lateinit var userService: UserService
  protected lateinit var controllerRepository: ControllerRepository
  protected lateinit var controllerService: ControllerService
  protected lateinit var admin: User

  @BeforeEach
  fun setupUserService() {
    userRepository = PostgresUserRepository(database)
    controllerRepository = PostgresControllerRepository(database)
    val passwordHashService = BcryptPasswordHashService()
    userService = UserService(userRepository, passwordHashService)
    controllerService = ControllerService(controllerRepository)

    runBlocking {
      admin = userService.createUser(UserPrototype(
        "admin@example.com",
        Password("vegard"),
        "Super",
        "Admin"
      ))
    }
  }

  fun withApp(block: TestApplicationEngine.(userRobot: UserHttpRobot) -> Unit) {
    withTestApplication({
      app(jwtConfig, userService, controllerService)
    }) {
      val userRobot = UserHttpRobot(this, jwtConfig, admin)
      block(userRobot)
    }
  }

  fun TestApplicationRequest.authenticated(user: User = admin) {
    addHeader(HttpHeaders.Authorization, "Bearer ${jwtConfig.createToken(user.id)}")
  }
}
