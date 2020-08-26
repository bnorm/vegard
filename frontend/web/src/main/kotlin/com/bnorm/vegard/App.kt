package com.bnorm.vegard

import com.bnorm.react.RFunction
import com.bnorm.vegard.auth.UserSessionProvider
import com.bnorm.vegard.components.Routes
import com.bnorm.vegard.service.RestVegardService
import com.bnorm.vegard.service.VegardServiceProvider
import react.RBuilder
import react.useRef

@Suppress("FunctionName")
@RFunction
fun RBuilder.App() {
  val service by useRef(RestVegardService())
  VegardServiceProvider(service) {
    UserSessionProvider(service) {
      Routes()
    }
  }
}
