package com.bnorm.vegard

import com.bnorm.vegard.model.User
import react.RDispatch
import react.createContext

sealed class UserAction {
  object Logout : UserAction() {
    override fun toString(): String = "Logout"
  }
  object Authenticating : UserAction() {
    override fun toString(): String = "Authenticating"
  }
  data class Login(val user: User) : UserAction()
}

sealed class UserState {
  object Unknown : UserState()
  object Unauthenticated : UserState()
  data class Authenticated(val session: UserSession): UserState()
}

val UserState.session: UserSession?
 get() = (this as? UserState.Authenticated)?.session

class UserSession(
  val user: User
)

val UserContext = createContext<Pair<UserState, RDispatch<UserAction>>>()
