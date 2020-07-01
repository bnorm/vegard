package com.bnorm.vegard.db

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ControllerTable : LongIdTable("vegard.controllers") {
  val serialNumber = text("serial_number").uniqueIndex()
  val macAddress = text("mac_address").uniqueIndex()
}

class ControllerEntity(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<ControllerEntity>(ControllerTable)

  var serialNumber by ControllerTable.serialNumber
  var macAddress by ControllerTable.macAddress
}
