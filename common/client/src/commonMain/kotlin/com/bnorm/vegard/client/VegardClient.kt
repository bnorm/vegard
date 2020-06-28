package com.bnorm.vegard.client

import com.bnorm.vegard.model.User
import com.bnorm.vegard.model.UserLoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.Url

interface AuthJwtStorage {
  fun retrieve(): String?
  fun store(jwt: String)
  fun clear()

  class Memory : AuthJwtStorage {
    private var jwt: String? = null

    override fun retrieve(): String? = jwt

    override fun store(jwt: String) {
      this.jwt = jwt
    }

    override fun clear() {
      jwt = null
    }
  }
}

fun VegardClient(
  url: Url,
  storage: AuthJwtStorage = AuthJwtStorage.Memory(),
  block: HttpClientConfig<*>.() -> Unit = {}
): VegardClient = VegardClient(url, storage, HttpClient {
  defaultRequest {
    storage.retrieve()?.let { jwt -> headers[HttpHeaders.Authorization] = "Bearer $jwt" }
  }
  install(JsonFeature) {
    serializer = KotlinxSerializer()
  }
  block()
})

class VegardClient(
  private val url: Url,
  private val storage: AuthJwtStorage,
  private val client: HttpClient
) {
  suspend fun login(request: UserLoginRequest) {
    val jwt = client.post<String>(url.appendPath("login")) {
      header(HttpHeaders.ContentType, "application/json")
      body = request
    }
    storage.store(jwt)
  }

  suspend fun logout() {
    storage.clear()
  }

  suspend fun getMe(): User {
    return client.get(url.appendPath("users", "me"))
  }
}

private fun Url.appendPath(vararg path: String) = when (encodedPath) {
  "/" -> copy(encodedPath = path.joinToString("/", prefix = "/"))
  else -> copy(encodedPath = "$encodedPath${path.joinToString("/", prefix = "/")}")
}
