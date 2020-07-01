package com.bnorm.vegard.db

import com.bnorm.vegard.model.PasswordHash
import com.bnorm.vegard.model.UserId
import com.google.inject.ImplementedBy
import kotlinx.coroutines.flow.Flow

@ImplementedBy(PostgresUserRepository::class)
interface UserRepository {
  suspend fun createUser(email: String, passwordHash: PasswordHash, firstName: String, lastName: String): UserEntity
  suspend fun findUserById(id: UserId): UserEntity?
  suspend fun findUserByEmail(email: String): UserEntity?
  fun getUsers(): Flow<UserEntity>
}
