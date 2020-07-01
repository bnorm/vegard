package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class Controller(
  val id: ControllerId,
  val serialNumber: String,
  val macAddress: String
)
