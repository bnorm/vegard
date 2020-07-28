package com.bnorm.vegard

import com.bnorm.vegard.auth.UserSessionProvider
import com.bnorm.vegard.service.RestVegardService
import com.bnorm.vegard.service.VegardServiceProvider
import com.bnorm.vegard.components.Routes
import react.RBuilder
import react.RProps
import react.rFunction

@Suppress("FunctionName")
fun RBuilder.App() = APP {}

private interface AppProps : RProps

private val APP = rFunction<AppProps>("App") {
  val service = RestVegardService()
  VegardServiceProvider(service) {
    UserSessionProvider(service) {
      Routes()
    }
  }
}
