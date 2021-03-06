package com.bnorm.vegard.db

import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.ControllerPrototype
import com.bnorm.vegard.model.ControllerReading
import com.bnorm.vegard.model.ControllerReadingPrototype
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import javax.inject.Inject

class PostgresControllerRepository @Inject constructor(
  private val database: Database
) : ControllerRepository {
  override suspend fun create(prototype: ControllerPrototype): ControllerEntity {
    return newSuspendedTransaction(db = database) {
      ControllerEntity.new {
        this.serialNumber = serialNumber
        this.macAddress = macAddress
      }
    }
  }

  override suspend fun findById(id: ControllerId): ControllerEntity? {
    return newSuspendedTransaction(db = database) { ControllerEntity.findById(id.value) }
  }

  override suspend fun findByMacAddress(macAddress: String): ControllerEntity? {
    return newSuspendedTransaction(db = database) {
      ControllerEntity.find { ControllerTable.macAddress eq macAddress }.singleOrNull()
    }
  }

  override fun getAll(): Flow<ControllerEntity> = channelFlow {
    newSuspendedTransaction(db = database) {
      for (entity in ControllerEntity.all()) {
        send(entity)
      }
    }
  }

  override suspend fun recordReading(id: ControllerId, prototype: ControllerReadingPrototype) {
    newSuspendedTransaction(db = database) {
      ControllerReadingTable.insert {
        it[controllerId] = EntityID(id.value, ControllerTable)
        it[timestamp] = Instant.now()
        it[ambientTemperature] = prototype.ambientTemperature
        it[ambientHumidity] = prototype.ambientHumidity
        it[soilMoisture] = prototype.soilMoisture
      }
    }
  }

  override fun getReadings(id: ControllerId, startTime: Instant, endTime: Instant): Flow<ControllerReading> = channelFlow {
    newSuspendedTransaction(db = database) {
      val query = ControllerReadingTable
        .select {
          (ControllerReadingTable.controllerId eq id.value) and
          (ControllerReadingTable.timestamp greaterEq startTime) and
            (ControllerReadingTable.timestamp less endTime)
        }
        .orderBy(ControllerReadingTable.timestamp)
      for (row in query) {
        send(ControllerReading(
          timestamp = row[ControllerReadingTable.timestamp],
          ambientTemperature = row[ControllerReadingTable.ambientTemperature],
          ambientHumidity = row[ControllerReadingTable.ambientHumidity],
          soilMoisture = row[ControllerReadingTable.soilMoisture]
        ))
      }
    }
  }
}
