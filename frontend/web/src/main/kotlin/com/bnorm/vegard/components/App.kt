package com.bnorm.vegard.components

import com.bnorm.vegard.UserAction
import com.bnorm.vegard.UserContext
import com.bnorm.vegard.UserSession
import com.bnorm.vegard.UserState
import com.bnorm.vegard.client.localJwtStorage
import com.bnorm.vegard.client.vegardClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RProps
import react.dom.div
import react.rFunction
import react.useEffect
import react.useReducer

@Suppress("FunctionName")
fun RBuilder.App() {
  APP {}
}

private interface AppProps : RProps

private val APP = rFunction<AppProps>("App") { props ->
  val initState = localJwtStorage.retrieve()?.let { UserState.Unknown } ?: UserState.Unauthenticated
  val (state, dispatch) = useReducer<UserState, UserAction>({ _, action ->
    when (action) {
      UserAction.Logout -> UserState.Unauthenticated
      UserAction.Authenticating -> UserState.Unknown
      is UserAction.Login -> UserState.Authenticated(UserSession(action.user))
    }
  }, initState)

  useEffect(emptyList()) {
    if (state == UserState.Unauthenticated) return@useEffect
    GlobalScope.launch {
      runCatching {
        val user = vegardClient.getMe()
        dispatch(UserAction.Login(user))
      }.onFailure {
        dispatch(UserAction.Logout)
      }
    }
  }

  val session = (state as? UserState.Authenticated)?.session
  UserContext.Provider(state to dispatch) {
    if (session != null) {
      Home()
    } else {
      if (state == UserState.Unauthenticated) {
        Login()
      } else {
        div {}
      }
    }
  }
}
