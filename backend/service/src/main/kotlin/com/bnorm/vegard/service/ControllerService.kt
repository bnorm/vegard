package com.bnorm.vegard.service

import com.bnorm.vegard.db.ControllerEntity
import com.bnorm.vegard.db.ControllerRepository
import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.model.ControllerAction
import com.bnorm.vegard.model.ControllerConnectRequest
import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.ControllerPrototype
import com.bnorm.vegard.model.ControllerReading
import com.bnorm.vegard.model.ControllerReadingPrototype
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class ControllerService @Inject constructor(
  private val controllerRepository: ControllerRepository
) {
  suspend fun create(prototype: ControllerPrototype): Controller {
    return controllerRepository.create(prototype).toController()
  }

  suspend fun findById(id: ControllerId): Controller? {
    return controllerRepository.findById(id)
      ?.toController()
  }

  suspend fun findByCredentials(credentials: ControllerConnectRequest): Controller? {
    return controllerRepository.findByMacAddress(credentials.macAddress)
      ?.takeIf { it.serialNumber == credentials.serialNumber }
      ?.toController()
  }

  fun getAll(): Flow<Controller> {
    return controllerRepository.getAll().map { it.toController() }
  }

  suspend fun recordReading(id: ControllerId, prototype: ControllerReadingPrototype) {
    controllerRepository.recordReading(id, prototype)
  }

  fun getRecordings(id: ControllerId, startTime: Instant, endTime: Instant = Instant.now()): Flow<ControllerReading> {
    return controllerRepository.getReadings(id, startTime, endTime)
  }

  suspend fun determineAction(controller: Controller): ControllerAction {
    return ControllerAction.Water(1)
  }

  private fun ControllerEntity.toController(): Controller {
    return Controller(ControllerId(id.value), serialNumber, macAddress)
  }
}
