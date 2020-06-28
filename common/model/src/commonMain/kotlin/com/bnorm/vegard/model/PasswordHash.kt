package com.bnorm.vegard.model

@RequiresOptIn
annotation class PasswordHashUsage

inline class PasswordHash(
  @property:PasswordHashUsage
  val value: String
) {
  override fun toString(): String = "*****"
}
