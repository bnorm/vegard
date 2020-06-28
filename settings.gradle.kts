pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    jcenter()
    maven { setUrl("https://kotlin.bintray.com/kotlinx") }
  }
}

rootProject.name = "vegard"

include(":backend:service")
include(":common:client")
include(":common:model")
include(":frontend:web")
