package com.bnorm.vegard.components

import com.bnorm.react.RFunction
import com.bnorm.vegard.auth.useUserSession
import kotlinx.html.js.onClickFunction
import materialui.components.button.button
import materialui.components.button.enums.ButtonColor
import materialui.components.button.enums.ButtonVariant
import react.RBuilder
import react.RProps
import react.rFunction

@Suppress("FunctionName")
@RFunction
fun RBuilder.LogoutButton() {
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
