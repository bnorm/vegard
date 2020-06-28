package com.bnorm.vegard.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies
import at.favre.lib.crypto.bcrypt.LongPasswordStrategy
import com.bnorm.vegard.model.Password
import com.bnorm.vegard.model.PasswordHash
import com.bnorm.vegard.model.PasswordHashUsage
import com.bnorm.vegard.model.PasswordUsage
import javax.inject.Inject

@OptIn(PasswordHashUsage::class, PasswordUsage::class)
class BcryptPasswordHashService @Inject constructor() : PasswordHashService {
  private val version: BCrypt.Version = BCrypt.Version.VERSION_2A
  private val longPasswordStrategy: LongPasswordStrategy = LongPasswordStrategies.hashSha512(version)
  private val cost = 12

  private val hasher: BCrypt.Hasher = BCrypt.with(version, longPasswordStrategy)
  private val verifyer: BCrypt.Verifyer = BCrypt.verifyer(version, longPasswordStrategy)

  override fun hash(password: Password): PasswordHash {
    return PasswordHash(hasher.hashToString(cost, password.value.toCharArray()))
  }

  override fun verify(password: Password, hash: PasswordHash): Boolean {
    val result = verifyer.verify(password.value.toCharArray(), hash.value)
    return result.verified
  }
}
