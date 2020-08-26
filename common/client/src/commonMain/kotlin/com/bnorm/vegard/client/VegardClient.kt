package com.bnorm.vegard.client

import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.ControllerReading
import com.bnorm.vegard.model.User
import com.bnorm.vegard.model.UserLoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url

class VegardClient(
  private val url: Url,
  client: HttpClient
) {
  private val client = client.config {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
  }

  suspend fun login(request: UserLoginRequest): String {
    return client.post(url.appendPath("login")) {
      header(HttpHeaders.ContentType, ContentType.Application.Json)
      body = request
    }
  }

  suspend fun refresh(): String {
    return client.post(url.appendPath("refresh")) {
      header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
  }

  suspend fun getMe(): User {
    return client.get(url.appendPath("users", "me"))
  }

  suspend fun getControllers(): List<Controller> {
    return client.get(url.appendPath("controllers"))
  }

  suspend fun getControllerRecords(id: ControllerId, startTime: String, endTime: String? = null): List<ControllerReading> {
    return client.get(url.appendPath("controllers", id.toString(), "records")
      .copy(parameters = Parameters.build {
        append("startTime", startTime)
        if (endTime != null) append("endTime", endTime)
      })
    )
  }
}

private fun Url.appendPath(vararg path: String) = when (encodedPath) {
  "/" -> copy(encodedPath = path.joinToString("/", prefix = "/"))
  else -> copy(encodedPath = "$encodedPath${path.joinToString("/", prefix = "/")}")
}
