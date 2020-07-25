package com.bnorm.vegard.components

import com.bnorm.vegard.auth.useUserSession
import kotlinx.html.js.onClickFunction
import materialui.components.button.button
import materialui.components.button.enums.ButtonColor
import materialui.components.button.enums.ButtonVariant
import react.RBuilder
import react.RProps
import react.rFunction

@Suppress("FunctionName")
fun RBuilder.LogoutButton() = LOGOUT_BUTTON {}

interface LogoutProps : RProps

val LOGOUT_BUTTON = rFunction<LogoutProps>("Logout") {
  val session = useUserSession()
  button {
    attrs {
      variant = ButtonVariant.contained
      color = ButtonColor.secondary
      onClickFunction = { session.logout() }
    }

    +"Logout"
  }
}
