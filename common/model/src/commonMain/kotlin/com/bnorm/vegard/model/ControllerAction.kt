package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
sealed class ControllerAction {
  @Serializable
  object Idle: ControllerAction()

  @Serializable
  class Water(val durationSeconds: Long) : ControllerAction()
}

@Serializable
class ControllerActionWrapper(
  val action: ControllerAction
)
