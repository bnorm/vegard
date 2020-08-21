package com.bnorm.vegard

import com.bnorm.vegard.api.ById
import com.bnorm.vegard.api.RecordsById
import com.bnorm.vegard.auth.ControllerPrincipal
import com.bnorm.vegard.auth.JwtService
import com.bnorm.vegard.auth.LoginFailureException
import com.bnorm.vegard.auth.UserPrincipal
import com.bnorm.vegard.auth.controllerId
import com.bnorm.vegard.auth.refresh
import com.bnorm.vegard.auth.userId
import com.bnorm.vegard.model.ControllerActionWrapper
import com.bnorm.vegard.model.ControllerConnectRequest
import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.ControllerPrototype
import com.bnorm.vegard.model.ControllerReadingPrototype
import com.bnorm.vegard.model.UserId
import com.bnorm.vegard.model.UserLoginRequest
import com.bnorm.vegard.model.UserPrototype
import com.bnorm.vegard.service.ControllerService
import com.bnorm.vegard.service.UserService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.auth.principal
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.features.DefaultHeaders
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resolveResource
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.util.DataConversionException
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID

fun Application.app(
  jwtService: JwtService,
  userService: UserService,
  controllerService: ControllerService
) {
  install(DefaultHeaders)
  install(Locations)

  install(CallLogging) {
    mdc("request.id") { UUID.randomUUID().toString().substring(0, 8) }
    logger = LoggerFactory.getLogger("com.bnorm.vegard.api.request")
  }

  install(DataConversion) {
    convert<Instant> {
      decode { values, _ ->
        values.singleOrNull()?.let { DateTimeFormatter.ISO_INSTANT.parse(it, Instant::from) }
      }

      encode { value ->
        when (value) {
          null -> listOf()
          is Instant -> listOf(DateTimeFormatter.ISO_INSTANT.format(value))
          else -> throw DataConversionException("Cannot convert $value as Instant")
        }
      }
    }
  }
  install(ContentNegotiation) {
    json(
      json = Json {
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
      }
    )
  }

  install(StatusPages) {
    exception<SerializationException> { cause ->
      call.respond(HttpStatusCode.BadRequest, cause.message ?: "Unknown error")
    }
    exception<LoginFailureException> { cause ->
      call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Unknown error")
    }
  }

  install(CORS) {
    method(HttpMethod.Put)
    method(HttpMethod.Delete)
    anyHost()
    allowNonSimpleContentTypes = true
    header(HttpHeaders.Authorization)
  }

  install(Authentication) {
    jwt("user_jwt") {
      verifier(jwtService.userVerifier)
      validate { credential ->
        val userId = credential.payload.userId
          ?: return@validate null

        val user = userService.findUserById(userId)
          ?: return@validate null

        UserPrincipal(user, credential.payload)
      }
    }
    jwt("controller_jwt") {
      verifier(jwtService.controllerVerifier)
      validate { credential ->
        val controllerId = credential.payload.controllerId
          ?: return@validate null

        val controller = controllerService.findById(controllerId)
          ?: return@validate null

        ControllerPrincipal(controller, credential.payload)
      }
    }
  }

  routing {
    static("/") {
      val pathParameter = "static-path"

      val resourcePackage = "web-ui"
      val fallbackPath = "index.html"

      get("{$pathParameter...}") {
        val relativePath = call.parameters.getAll(pathParameter)?.joinToString(File.separator) ?: return@get
        if (relativePath.startsWith("api${File.separator}")) return@get
        val content = call.resolveResource(relativePath, resourcePackage)
          ?: call.resolveResource(fallbackPath, resourcePackage)
        if (content != null) call.respond(content)
      }
    }

    route("api/v1/") {
      post("login") {
        val credentials = call.receive<UserLoginRequest>()
        val user = userService.findUserByCredentials(credentials)
          ?: throw LoginFailureException("unknown user credentials")
        val token = jwtService.createToken(user.id, 14)
        call.respondText(token)
      }

      authenticate("user_jwt") {
        post("refresh") {
          val principal = call.principal<UserPrincipal>()!!
          val remaining = principal.jwt.refresh
          if (remaining <= 0) throw LoginFailureException("unable to refresh credentials")
          val token = jwtService.createToken(principal.user.id, remaining - 1)
          call.respondText(token)
        }
      }

      post("connect") {
        val credentials = call.receive<ControllerConnectRequest>()
        val controller = controllerService.findByCredentials(credentials)
          ?: throw LoginFailureException("unknown controller credentials")
        val token = jwtService.createToken(controller.id)
        call.respondText(token)
      }

      authenticate("user_jwt") {
        route("users") {
          post {
            val prototype = call.receive<UserPrototype>()
            call.respond(userService.createUser(prototype))
          }
          get {
            call.respond(userService.getUsers().toList())
          }
          get("me") {
            call.respond(call.principal<UserPrincipal>()!!.user)
          }
          get<ById> { (id) ->
            val user = userService.findUserById(UserId(id))
              ?: throw NotFoundException("unknown user with id = $id")
            call.respond(user)
          }
        }
      }

      route("controllers") {
        // Only controllers can record readings
        authenticate("controller_jwt") {
          get("me") {
            call.respond(call.principal<ControllerPrincipal>()!!.controller)
          }
          get("action") {
            val controller = call.principal<ControllerPrincipal>()!!.controller
            call.respond(ControllerActionWrapper(controllerService.determineAction(controller)))
          }
          post("record") {
            val controller = call.principal<ControllerPrincipal>()!!.controller
            val reading = call.receive<ControllerReadingPrototype>()
            call.respond(controllerService.recordReading(controller.id, reading))
          }
        }

        // Users can view and create controllers
        authenticate("user_jwt") {
          post {
            val prototype = call.receive<ControllerPrototype>()
            call.respond(controllerService.create(prototype))
          }
          get {
            call.respond(controllerService.getAll().toList())
          }
          get<ById> { (id) ->
            val controller = controllerService.findById(ControllerId(id))
              ?: throw NotFoundException("unknown controller with id = $id")
            call.respond(controller)
          }
          get<RecordsById> { (id, startTime, endTime) ->
            val readings = controllerService.getRecordings(ControllerId(id), startTime, endTime)
            call.respond(readings.toList())
          }
        }
      }
    }
  }
}
