package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class ControllerConnectRequest(
  val serialNumber: String,
  val macAddress: String
)
