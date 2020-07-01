package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class ControllerPrototype(
  val serialNumber: String,
  val macAddress: String
)
