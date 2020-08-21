import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  kotlin("js")
}

kotlin {
  js {
    browser {
      useCommonJs()
      runTask {
        // TODO: use dsl after KT-32016 will be fixed
        devServer = KotlinWebpackConfig.DevServer(
          port = 8081,
          proxy = mapOf("/api/v1/**" to "http://localhost:8080"),
          contentBase = listOf("$projectDir/src/main/resources")
        )
        outputFileName = "web.js"
      }
      // TODO: https://youtrack.jetbrains.com/issue/KTOR-541
      dceTask {
        keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
      }
    }
  }
}

dependencies {
  implementation(project(":common:model"))
  implementation(project(":common:client"))

  implementation(kotlin("stdlib-js"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.9")

  implementation("io.ktor:ktor-client-auth-js:1.4.0")

  implementation("org.jetbrains:kotlin-extensions:1.0.1-pre.112-kotlin-1.4.0")
  implementation("org.jetbrains:kotlin-react:16.13.1-pre.112-kotlin-1.4.0")
  implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.112-kotlin-1.4.0")
  implementation("org.jetbrains:kotlin-react-router-dom:5.1.2-pre.112-kotlin-1.4.0")

  implementation("subroh0508.net.kotlinmaterialui:core:0.5.0-beta2")

  implementation("io.github.microutils:kotlin-logging-js:1.8.3")

  implementation(npm("core-js", "3.2.0"))
  implementation(npm("react", "16.13.1"))
  implementation(npm("react-dom", "16.13.1"))
  implementation(npm("react-router-dom", "5.1.2"))

  implementation(npm("@devexpress/dx-react-core", "2.7.0"))
  implementation(npm("@devexpress/dx-react-chart", "2.7.0"))
  implementation(npm("@devexpress/dx-react-chart-material-ui", "2.7.0"))

  implementation(npm("inline-style-prefixer", "5.1.0"))
  implementation(npm("styled-components", "4.4.0"))

  testImplementation(kotlin("test-js"))
  testImplementation(npm("karma", "4.4.1"))
  testImplementation(npm("enzyme", "3.11.0"))
  testImplementation(npm("enzyme-adapter-react-16", "1.15.3"))
}

tasks.register<Sync>("jsBundle") {
  dependsOn(tasks.named("browserDevelopmentWebpack"))
  from(tasks.named("browserDistributeResources"))
  into("$buildDir/bundle")
}
