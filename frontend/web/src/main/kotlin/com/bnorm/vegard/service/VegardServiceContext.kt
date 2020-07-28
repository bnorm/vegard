package com.bnorm.vegard.service

import react.RBuilder
import react.createContext
import react.useContext

fun useVegardService(): VegardService = useContext(VegardServiceContext)

private val VegardServiceContext = createContext<VegardService>()

fun RBuilder.VegardServiceProvider(
  service: VegardService,
  block: RBuilder.(VegardService) -> Unit
) {
  VegardServiceContext.Provider(service) {
    block(service)
  }
}
