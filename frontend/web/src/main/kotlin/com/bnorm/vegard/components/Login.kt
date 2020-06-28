package com.bnorm.vegard.components

import com.bnorm.vegard.UserAction
import com.bnorm.vegard.UserContext
import com.bnorm.vegard.client.vegardClient
import com.bnorm.vegard.model.Password
import com.bnorm.vegard.model.UserLoginRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.input
import react.dom.label
import react.getValue
import react.rFunction
import react.setValue
import react.useEffect
import react.useState
import kotlin.browser.window

@Suppress("FunctionName")
fun RBuilder.Login() {
  LOGIN {}
}

private interface LoginProps : RProps

private val LOGIN = rFunction<LoginProps>("Login") {
  var email by useState("")
  var password by useState("")

  // automatically redirect to root
  useEffect {
    if (window.location.pathname != "/") {
      window.location.href = "/"
    }
  }
  if (window.location.pathname != "/") {
    div {}
    return@rFunction
  }

  UserContext.Consumer { (_, dispatch) ->
    fun validateForm() = email.isNotEmpty() && password.isNotEmpty()
    fun handleSubmit(event: Event) {
      event.preventDefault()

      GlobalScope.launch {
        dispatch(UserAction.Authenticating)
        runCatching {
          vegardClient.login(UserLoginRequest(email, Password(password)))
          val user = vegardClient.getMe()
          dispatch(UserAction.Login(user))
        }.onFailure {
          dispatch(UserAction.Logout)
        }
      }
    }

    div(classes = "container-sm") {
      form {
        attrs.onSubmitFunction = { handleSubmit(it) }
        div(classes = "form-group") {
          label { +"Email" }
          input(classes = "form-control", type = InputType.text) {
            attrs.placeholder = "email"
            attrs.value = email
            attrs.onChangeFunction = { email = (it.target as HTMLInputElement).value }
          }
        }
        div(classes = "form-group") {
          label { +"Password" }
          input(classes = "form-control", type = InputType.password) {
            attrs.placeholder = "password"
            attrs.value = password
            attrs.onChangeFunction = { password = (it.target as HTMLInputElement).value }
          }
        }
        button(classes = "btn btn-primary", type = ButtonType.submit) {
          attrs.disabled = !validateForm()
          +"Login"
        }
      }
    }
  }
}
