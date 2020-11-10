plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  jvm()
  js(IR) {
    browser()
  }

  sourceSets {
    named("commonMain") {
      dependencies {
        api(project(":common:model"))
        api("io.ktor:ktor-client-core:1.4.2")
        implementation("io.ktor:ktor-client-json:1.4.2")
        implementation("io.ktor:ktor-client-serialization:1.4.2")
      }
    }
    named("jvmMain") {
      dependencies {
        implementation("io.ktor:ktor-client-okhttp:1.4.2")
      }
    }
  }
}
