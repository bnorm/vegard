package com.bnorm.vegard

import com.bnorm.vegard.auth.UserSessionProvider
import com.bnorm.vegard.components.Routes
import com.bnorm.vegard.service.RestVegardService
import com.bnorm.vegard.service.VegardServiceProvider
import react.RBuilder
import react.RProps
import react.rFunction
import react.useRef

@Suppress("FunctionName")
fun RBuilder.App() = APP {}

private interface AppProps : RProps

private val APP = rFunction<AppProps>("App") {
  val service by useRef(RestVegardService())
  VegardServiceProvider(service) {
    UserSessionProvider(service) {
      Routes()
    }
  }
}
