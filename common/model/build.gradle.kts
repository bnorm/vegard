import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0-1.4.0-dev-5730")
      }
    }
    named("jvmMain") {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0-1.4.0-dev-5730")
      }
    }
    named("jsMain") {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.20.0-1.4.0-dev-5730")
      }
    }
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "11"
    freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-XXLanguage:+InlineClasses")
//    if ("test" in name.toLowerCase() || "integration" in name.toLowerCase()) {
//      useIR = true
//    }
  }
}
