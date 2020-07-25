package com.bnorm.vegard.auth

import com.bnorm.vegard.client.localJwtStorage
import com.bnorm.vegard.client.vegardClient
import com.bnorm.vegard.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RDispatch
import react.createContext
import react.useContext
import react.useEffect
import react.useReducer

private sealed class UserAction {
  object Logout : UserAction() {
    override fun toString(): String = "Logout"
  }
  object Authenticating : UserAction() {
    override fun toString(): String = "Authenticating"
  }
  data class Login(val user: User) : UserAction()
}

private sealed class UserState {
  object Unknown : UserState()
  object Unauthenticated : UserState()
  data class Authenticated(
    val user: User
  ): UserState()
}

interface UserSession {
  val unauthenticated: Boolean
  val user: User?

  fun authenticating()
  fun login(user: User)
  fun logout()
}

private class RealUserSession(
  private val state: UserState,
  private val dispatch: RDispatch<UserAction>
) : UserSession {
  override val unauthenticated: Boolean
    get() = state is UserState.Unauthenticated
  override val user: User?
    get() = (state as? UserState.Authenticated)?.user

  override fun authenticating() {
    dispatch(UserAction.Authenticating)
  }

  override fun login(user: User) {
    dispatch(UserAction.Login(user))
  }

  override fun logout() {
    vegardClient.logout()
    dispatch(UserAction.Logout)
  }
}

private val UserContext = createContext<UserSession>()

fun RBuilder.UserSessionProvider(block: RBuilder.(UserSession) -> Unit) {
  val initState = localJwtStorage.retrieve()?.let { UserState.Unknown } ?: UserState.Unauthenticated
  val (state, dispatch) = useReducer<UserState, UserAction>({ _, action ->
    when (action) {
      UserAction.Logout -> UserState.Unauthenticated
      UserAction.Authenticating -> UserState.Unknown
      is UserAction.Login -> UserState.Authenticated(action.user)
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

  val session = RealUserSession(state, dispatch)
  UserContext.Provider(session) {
    block(session)
  }
}

fun useUserSession(): UserSession = useContext(UserContext)
