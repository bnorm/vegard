package com.bnorm.vegard.components

import com.bnorm.vegard.auth.useUserSession
import materialui.components.typography.enums.TypographyVariant
import materialui.components.typography.typography
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route

@Suppress("FunctionName")
fun RBuilder.Routes() = child(ROUTES) {}

private interface RoutesProps : RProps

private val ROUTES = functionalComponent<RoutesProps> {
  val session = useUserSession()
  browserRouter {
    if (session.user != null) {
      route(path = "/home") {
        Layout(
          toolbar = { toolbar() },
          sidebar = { sidebar() },
          details = { +"Details" }
        ) {
          Home()
        }
      }
      redirect(from = "/", to = "/home", exact = true)
      redirect(from = "/login", to = "/home", exact = true)
    } else if (session.unauthenticated) {
      route(path = "/login") { Login() }
      redirect(from = "/", to = "/login", exact = true)
      redirect(from = "/home", to = "/login", exact = true)
    }
  }
}

private fun RBuilder.toolbar() {
  typography {
    attrs {
      variant = TypographyVariant.h6
      noWrap = true
    }

    +"Vegard"
  }
}

private fun RBuilder.sidebar() {
  div {
    +"Sidebar"
  }

  LogoutButton()
}
