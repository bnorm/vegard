package com.bnorm.vegard.db

import com.bnorm.vegard.model.PasswordHash
import com.bnorm.vegard.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject

class PostgresUserRepository @Inject constructor(
  private val database: Database
) : UserRepository {
  override suspend fun createUser(
    email: String,
    passwordHash: PasswordHash,
    firstName: String,
    lastName: String
  ): UserEntity {
    return newSuspendedTransaction(db = database) {
      UserEntity.new {
        this.email = email
        this.password = passwordHash
        this.firstName = firstName
        this.lastName = lastName
      }
    }
  }

  override suspend fun findUserById(id: UserId): UserEntity? {
    return newSuspendedTransaction(db = database) { UserEntity.findById(id.value) }
  }

  override suspend fun findUserByEmail(email: String): UserEntity? {
    return newSuspendedTransaction(db = database) {
      UserEntity.find { UserTable.email eq email }.singleOrNull()
    }
  }

  override fun getUsers(): Flow<UserEntity> = channelFlow {
    newSuspendedTransaction(db = database) {
      for (entity in UserEntity.all()) {
        send(entity)
      }
    }
  }
}
