package com.bnorm.vegard.inject

import com.google.inject.AbstractModule
import com.google.inject.Injector
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationEnvironment
import io.ktor.application.call
import io.ktor.config.ApplicationConfig
import io.ktor.util.AttributeKey

//fun Application.installGuice(injector: Injector) {
//  intercept(ApplicationCallPipeline.Features) {
//    call.attributes.put(InjectorKey, injector.createChildInjector(CallModule(call)))
//  }
//}
//
//private val InjectorKey = AttributeKey<Injector>("injector")
//val ApplicationCall.injector: Injector get() = attributes[InjectorKey]
//
//class CallModule(private val call: ApplicationCall) : AbstractModule() {
//  override fun configure() {
//    bind(ApplicationCall::class.java).toInstance(call)
//  }
//}

class ApplicationModule(private val application: Application) : AbstractModule() {
  override fun configure() {
    bind(Application::class.java).toInstance(application)
    bind(ApplicationEnvironment::class.java).toInstance(application.environment)
    bind(ApplicationConfig::class.java).toInstance(application.environment.config)
  }
}
