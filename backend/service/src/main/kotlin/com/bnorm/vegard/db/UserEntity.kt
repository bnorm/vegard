package com.bnorm.vegard.db

import com.bnorm.vegard.model.PasswordHash
import com.bnorm.vegard.model.PasswordHashUsage
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object UserTable : LongIdTable("vegard.users") {
  val email = text("email").uniqueIndex()
  val passwordHash = text("password")
  val firstName = text("first_name")
  val lastName = text("last_name")
}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<UserEntity>(UserTable)

  var email by UserTable.email
  private var passwordHash by UserTable.passwordHash
  var firstName by UserTable.firstName
  var lastName by UserTable.lastName

  @OptIn(PasswordHashUsage::class)
  var password: PasswordHash
    get() = PasswordHash(passwordHash)
    set(value) {
      passwordHash = value.value
    }
}
