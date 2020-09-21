import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC2")
      }
    }
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "11"
    freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-XXLanguage:+InlineClasses")
//    useIR = true
  }
}
