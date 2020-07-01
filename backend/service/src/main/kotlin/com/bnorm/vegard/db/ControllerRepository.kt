package com.bnorm.vegard.db

import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.ControllerPrototype
import com.bnorm.vegard.model.ControllerReading
import com.bnorm.vegard.model.PasswordHash
import com.bnorm.vegard.model.UserId
import com.google.inject.ImplementedBy
import kotlinx.coroutines.flow.Flow

@ImplementedBy(PostgresControllerRepository::class)
interface ControllerRepository {
  suspend fun create(prototype: ControllerPrototype): ControllerEntity
  suspend fun findById(id: ControllerId): ControllerEntity?
  suspend fun findByMacAddress(macAddress: String): ControllerEntity?
  fun getAll(): Flow<ControllerEntity>

  suspend fun recordReading(id: ControllerId, controllerReading: ControllerReading)
}
