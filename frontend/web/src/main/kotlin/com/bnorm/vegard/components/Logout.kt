package com.bnorm.vegard.components

import com.bnorm.vegard.UserAction
import com.bnorm.vegard.UserContext
import com.bnorm.vegard.client.vegardClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.dom.button
import react.rFunction

@Suppress("FunctionName")
fun RBuilder.Logout() {
  LOGOUT {}
}

interface LogoutProps : RProps

val LOGOUT = rFunction<LogoutProps>("Logout") { _ ->
  UserContext.Consumer { (_, dispatch) ->
    button(classes = "btn btn-danger my-2 my-sm-0") {
      +"Logout"
      attrs.onClickFunction = {
        GlobalScope.launch {
          vegardClient.logout()
          dispatch(UserAction.Logout)
        }
      }
    }
  }
}
