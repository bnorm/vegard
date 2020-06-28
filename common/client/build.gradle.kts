plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  jvm()
  js {
    browser()
    nodejs()
  }

  sourceSets {
    named("commonMain") {
      dependencies {
        implementation(kotlin("stdlib"))

        api(project(":common:model"))
        api("io.ktor:ktor-client-core:1.3.2")

        implementation("io.ktor:ktor-client-json:1.3.2")
        implementation("io.ktor:ktor-client-serialization:1.3.2")
      }
    }
    named("jvmMain") {
      dependencies {
        implementation(kotlin("stdlib-jdk8"))

        api("io.ktor:ktor-client:1.3.2")

        implementation("io.ktor:ktor-client-okhttp:1.3.2")
        implementation("io.ktor:ktor-client-json-jvm:1.3.2")
        implementation("io.ktor:ktor-client-serialization-jvm:1.3.2")
      }
    }
    named("jsMain") {
      dependencies {
        implementation(kotlin("stdlib-js"))

        api("io.ktor:ktor-client-js:1.3.2")

        implementation("io.ktor:ktor-client-json-js:1.3.2")
        implementation("io.ktor:ktor-client-serialization-js:1.3.2")

        implementation(npm("text-encoding", "0.7.0"))
        implementation(npm("abort-controller", "3.0.0"))
        implementation(npm("utf-8-validate", "5.0.2"))
        implementation(npm("bufferutil", "4.0.1"))
        implementation(npm("fs", "0.0.2"))
      }
    }
  }
}
