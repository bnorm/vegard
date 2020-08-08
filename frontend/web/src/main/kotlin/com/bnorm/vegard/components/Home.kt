package com.bnorm.vegard.components

import com.bnorm.vegard.auth.useUserSession
import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.service.useVegardService
import com.bnorm.vegard.useMainScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RProps
import react.dom.div
import react.dom.h1
import react.getValue
import react.rFunction
import react.setValue
import react.useEffect
import react.useState
import kotlin.js.Date

fun RBuilder.Home() = HOME {}

private interface HomeProps : RProps

private val HOME = rFunction<HomeProps>("Home") {
  val service = useVegardService()
  val session = useUserSession()
  val scope = useMainScope(name = "HomeScope")

  var controllers by useState<List<Controller>?>(null)
  useEffect(emptyList()) {
    scope.launch {
      controllers = service.getControllers()
    }
  }

  div {
    h1 {
      +"Hello, ${session.user?.firstName}"
    }

    if (controllers == null) {
      div { +"Loading controllers..." }
    } else {
      val startTime = Date("2020-08-02T00:00:00.000Z")
      for (controller in controllers!!) {
        ControllerChart(controller, startTime)
      }
    }
  }
}
