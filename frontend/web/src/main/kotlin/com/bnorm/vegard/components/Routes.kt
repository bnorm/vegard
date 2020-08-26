package com.bnorm.vegard.components

import com.bnorm.react.RFunction
import com.bnorm.vegard.auth.useUserSession
import materialui.components.typography.enums.TypographyVariant
import materialui.components.typography.typography
import react.Fragment
import react.RBuilder
import react.dom.div
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route

@Suppress("FunctionName")
@RFunction
fun RBuilder.Routes() {
  val session = useUserSession()
  browserRouter {
    if (session.user != null) {
      route(path = "/home") {
        Fragment {
          Layout(
            toolbar = { toolbar() },
            sidebar = { sidebar() },
            details = { +"Details" }
          ) {
            Home()
          }
        }
      }
      redirect(from = "/", to = "/home", exact = true)
      redirect(from = "/login", to = "/home", exact = true)
    } else if (session.unauthenticated) {
      route(path = "/login") { Fragment { Login() } }
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
