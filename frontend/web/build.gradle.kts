import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  kotlin("js")
  id("com.bnorm.react.kotlin-react-function")
}

kotlin {
  js(IR) {
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
    }
    binaries.executable()
  }
}

dependencies {
  implementation(project(":common:model"))
  implementation(project(":common:client"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.9")

  implementation("io.ktor:ktor-client-auth-js:1.4.0")

  implementation("com.bnorm.react:kotlin-react-function:0.2.0")
  implementation("org.jetbrains:kotlin-extensions:1.0.1-pre.117-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react:16.13.1-pre.117-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.117-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react-router-dom:5.1.2-pre.117-kotlin-1.4.10")

  implementation("subroh0508.net.kotlinmaterialui:core:0.5.0-beta3")

  implementation(npm("@devexpress/dx-react-core", "2.7.0"))
  implementation(npm("@devexpress/dx-react-chart", "2.7.0"))
  implementation(npm("@devexpress/dx-react-chart-material-ui", "2.7.0"))

  testImplementation(kotlin("test-js"))
  testImplementation(npm("enzyme", "3.11.0"))
  testImplementation(npm("enzyme-adapter-react-16", "1.15.3"))
}

tasks.register<Sync>("jsBundle") {
  dependsOn(tasks.named("browserDevelopmentWebpack"))
  from(tasks.named("browserDistributeResources"))
  into("$buildDir/bundle")
}
