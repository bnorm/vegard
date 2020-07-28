package com.bnorm.vegard.service

import com.bnorm.vegard.client.VegardClient
import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.ControllerReading
import com.bnorm.vegard.model.User
import com.bnorm.vegard.model.UserLoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import kotlin.browser.window

val vegardApiUrl = URLBuilder(window.location.toString()).apply {
  path("api", "v1")

  // Sanitize other URL properties
  user = null
  password = null
  fragment = ""
  parameters.clear()
  trailingQuery = false
}.build()


class RestVegardService(
  url: Url = vegardApiUrl
) : VegardService {
  private var jwt: String? = null

  private val client = VegardClient(
    url = url,
    client = HttpClient {
      defaultRequest {
        jwt?.let { headers[HttpHeaders.Authorization] = "Bearer $it" }
      }
    }
  )

  override val authenticated: Boolean
    get() = jwt != null // TODO check expired

  override suspend fun login(request: UserLoginRequest): String = client.login(request)

  override fun logout() {
    jwt = null
  }

  override suspend fun getMe(): User = client.getMe()

  override suspend fun getControllers(): List<Controller> = client.getControllers()

  override suspend fun getControllerRecords(id: ControllerId, startTime: String, endTime: String?): List<ControllerReading> =
    client.getControllerRecords(id, startTime, endTime)
}
