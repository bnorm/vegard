package com.bnorm.vegard.auth

import com.bnorm.vegard.model.Password
import com.bnorm.vegard.model.PasswordHash
import com.google.inject.ImplementedBy

@ImplementedBy(BcryptPasswordHashService::class)
interface PasswordHashService {
  fun hash(password: Password): PasswordHash
  fun verify(password: Password, hash: PasswordHash): Boolean
}
