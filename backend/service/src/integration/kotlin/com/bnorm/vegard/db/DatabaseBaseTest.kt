package com.bnorm.vegard.db

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

class KPostgresContainer : PostgreSQLContainer<KPostgresContainer>("postgres:12.3")

@Testcontainers
abstract class DatabaseBaseTest {
  companion object {
    @JvmField
    @Container
    val postgres: KPostgresContainer = KPostgresContainer()
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("postgres")
  }

  protected lateinit var database: Database

  @BeforeEach
  fun setupSchema() {
    Flyway.configure()
      .schemas("vegard")
      .dataSource(postgres.jdbcUrl, postgres.username, postgres.password)
      .load()
      .migrate()

    database = Database.connect(url = postgres.jdbcUrl, user = postgres.username, password = postgres.password)
  }

  @AfterEach
  fun teardownSchema() {
    postgres.createConnection("").use { connection ->
      connection.createStatement().execute("drop schema vegard cascade")
    }
  }
}
