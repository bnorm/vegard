package com.bnorm.vegard.service

import com.bnorm.vegard.model.UserLoginRequest
import com.bnorm.vegard.auth.PasswordHashService
import com.bnorm.vegard.db.UserEntity
import com.bnorm.vegard.db.PostgresUserRepository
import com.bnorm.vegard.model.User
import com.bnorm.vegard.model.UserId
import com.bnorm.vegard.model.UserPrototype
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserService @Inject constructor(
  private val postgresUserRepository: PostgresUserRepository,
  private val passwordHashService: PasswordHashService
) {
  suspend fun createUser(prototype: UserPrototype): User {
    return postgresUserRepository.createUser(
      prototype.email,
      passwordHashService.hash(prototype.password),
      prototype.firstName,
      prototype.lastName
    ).toUser()
  }

  suspend fun findUserById(id: UserId): User? {
    return postgresUserRepository.findUserById(id)
      ?.toUser()
  }

  suspend fun findUserByCredentials(credential: UserLoginRequest): User? {
    return postgresUserRepository.findUserByEmail(credential.email)
      ?.takeIf { passwordHashService.verify(credential.password, it.password) }
      ?.toUser()
  }

  fun getUsers(): Flow<User> {
    return postgresUserRepository.getUsers().map { it.toUser() }
  }

  private fun UserEntity.toUser(): User {
    return User(UserId(id.value), email, firstName, lastName)
  }
}
