package com.bnorm.vegard.inject

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment
import io.ktor.config.ApplicationConfig
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

class DatabaseModule : AbstractModule() {

  @Provides
  fun provideFlyway(config: ApplicationConfig): Flyway {
    val postgresJdbcUrl = config.property("postgres.jdbcUrl").getString()
    val postgresUsername = config.property("postgres.username").getString()
    val postgresPassword = config.property("postgres.password").getString()

    return Flyway.configure()
      .schemas("vegard")
      .dataSource(postgresJdbcUrl, postgresUsername, postgresPassword)
      .load()
  }

  @Provides
  fun provideDatabase(config: ApplicationConfig): Database {
    val postgresJdbcUrl = config.property("postgres.jdbcUrl").getString()
    val postgresUsername = config.property("postgres.username").getString()
    val postgresPassword = config.property("postgres.password").getString()

    return Database.connect(url = postgresJdbcUrl, user = postgresUsername, password = postgresPassword)
  }
}
