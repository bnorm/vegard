package com.bnorm.vegard.service

import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.model.ControllerId
import com.bnorm.vegard.model.ControllerReading
import com.bnorm.vegard.model.User
import com.bnorm.vegard.model.UserLoginRequest

interface VegardService {
  val authenticated: Boolean

  suspend fun login(request: UserLoginRequest): String
  suspend fun refresh(): String
  fun logout()
  suspend fun getMe(): User
  suspend fun getControllers(): List<Controller>
  suspend fun getControllerRecords(id: ControllerId, startTime: String, endTime: String? = null): List<ControllerReading>
}

