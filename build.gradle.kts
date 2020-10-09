plugins {
  kotlin("jvm") version "1.4.0" apply false
  kotlin("js") version "1.4.0" apply false
  kotlin("multiplatform") version "1.4.0" apply false
  kotlin("plugin.serialization") version "1.4.0" apply false
  kotlin("plugin.jpa") version "1.4.0" apply false
  id("com.bnorm.power.kotlin-power-assert") version "0.5.3" apply false
  id("com.bnorm.react.kotlin-react-function") version "0.2.1" apply false

  idea
}

allprojects {
  group = "com.bnorm.vegard"
  version = "0.1-SNAPSHOT"

  repositories {
    mavenCentral()
    jcenter()
    maven { setUrl("https://kotlin.bintray.com/kotlinx") }
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
  }
}

idea {
  module {
    // Ignore firmware folder since it is developed using CLion
    excludeDirs = setOf(project.file("firmware"))
  }
}
