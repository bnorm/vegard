package com.bnorm.vegard.client

import io.ktor.http.URLBuilder
import org.w3c.dom.Location
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.window

class LocalAuthJwtStorage(
  private val key: String = "jwt"
) : AuthJwtStorage {
  override fun retrieve(): String? {
    return window.localStorage[key]
  }

  override fun store(jwt: String) {
    window.localStorage[key] = jwt
  }

  override fun clear() {
    window.localStorage.removeItem(key)
  }
}

fun Location.toUrlBuilder(): URLBuilder {
  return URLBuilder(toString())
}

val localJwtStorage = LocalAuthJwtStorage()
val vegardClient = VegardClient(
  window.location.toUrlBuilder().apply {
    path("api", "v1")

    // Sanitize other URL properties
    user = null
    password = null
    fragment = ""
    parameters.clear()
    trailingQuery = false
  }.build(),
  localJwtStorage
)
