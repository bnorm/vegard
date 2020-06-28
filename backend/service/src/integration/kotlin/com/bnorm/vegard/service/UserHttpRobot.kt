package com.bnorm.vegard.service

import com.bnorm.vegard.auth.JwtService
import com.bnorm.vegard.model.User
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals

class UserHttpRobot(
  private val engine: TestApplicationEngine,
  jwtService: JwtService,
  user: User
) {
  private val authorization: String = "Bearer ${jwtService.createToken(user.id)}"

  fun createUser(
    email: String? = null,
    password: String? = null,
    firstName: String? = null,
    lastName: String? = null
  ): UserResponse {
    val call = engine.handleRequest(HttpMethod.Post, "/api/v1/users") {
      addHeader(HttpHeaders.Authorization, authorization)
      addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())

      val emailJson = if (email != null) """"email": "$email"""" else null
      val passwordJson = if (password != null) """"password": "$password"""" else null
      val firstNameJson = if (firstName != null) """"firstName": "$firstName"""" else null
      val lastNameJson = if (lastName != null) """"lastName": "$lastName"""" else null

      setBody(
        listOfNotNull(emailJson, passwordJson, firstNameJson, lastNameJson)
          .joinToString(separator = ", ", prefix = "{", postfix = "}")
      )
    }

    return UserResponse(call)
  }

  fun getUser(
    id: Any
  ): UserResponse {
    val call = engine.handleRequest(HttpMethod.Get, "/api/v1/users/$id") {
      addHeader(HttpHeaders.Authorization, authorization)
    }

    return UserResponse(call)
  }
}

class UserResponse(
  private val call: TestApplicationCall
) {
  fun success(): User {
    with(call) {
      assertEquals(HttpStatusCode.OK, response.status())
      return Json.parse(User.serializer(), response.content!!)
    }
  }

  fun badRequest() {
    with(call) {
      assertEquals(HttpStatusCode.BadRequest, response.status())
    }
  }

  fun notFound() {
    with(call) {
      assertEquals(HttpStatusCode.NotFound, response.status())
    }
  }
}
