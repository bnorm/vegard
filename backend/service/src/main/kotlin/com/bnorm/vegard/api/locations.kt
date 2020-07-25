@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.bnorm.vegard.api

import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import java.time.Instant

@Location("/{id}")
data class ById(val id: Long)

@Location("/paged")
data class Paged(val page: Int = 0, val count: Int = 100)

@Location("/{id}/records")
data class RecordsById(
  val id: Long,
  val startTime: Instant,
  val endTime: Instant = Instant.now()
)
