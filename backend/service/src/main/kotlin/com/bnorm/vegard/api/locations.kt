@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.bnorm.vegard.api

import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@Location("/{id}")
data class ById(val id: Long)

@Location("/paged")
data class Paged(val page: Int = 0, val count: Int = 100)
