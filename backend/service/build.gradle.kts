import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  kotlin("plugin.jpa")
  application
  id("com.bnorm.power.kotlin-power-assert")
}

sourceSets {
  create("integration") {
    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
  }
}

val integrationImplementation by configurations.getting {
  extendsFrom(configurations.testImplementation.get())
}
configurations["integrationRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  implementation(project(":common:model"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.7")

  implementation("com.google.inject:guice:4.2.3:no_aop")

  implementation("io.ktor:ktor-server-netty:1.3.2")
  implementation("io.ktor:ktor-locations:1.3.2")
  implementation("io.ktor:ktor-websockets:1.3.2")
  implementation("io.ktor:ktor-serialization:1.3.2")
  implementation("io.ktor:ktor-auth:1.3.2")
  implementation("io.ktor:ktor-auth-jwt:1.3.2")

  implementation("at.favre.lib:bcrypt:0.9.0")

  implementation("org.jetbrains.exposed:exposed-dao:0.26.1")
  implementation("org.jetbrains.exposed:exposed-java-time:0.26.1")
  runtimeOnly("org.jetbrains.exposed:exposed-jdbc:0.26.1")
  implementation("org.flywaydb:flyway-core:6.5.0")
  runtimeOnly("org.postgresql:postgresql:42.2.14")

  implementation("tech.units:indriya:2.0.2")

  implementation("org.apache.logging.log4j:log4j-api:2.13.3")
  runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.13.3")
  runtimeOnly("org.apache.logging.log4j:log4j-core:2.13.3")

  // Unit tests
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

  testImplementation("io.ktor:ktor-server-test-host:1.3.2")

  // Integration tests
  integrationImplementation("org.testcontainers:testcontainers:1.14.3")
  integrationImplementation("org.testcontainers:junit-jupiter:1.14.3")
  integrationImplementation("org.testcontainers:postgresql:1.14.3")
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

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
  }
}

val integrationTest = task<Test>("integrationTest") {
  description = "Runs integration tests."
  group = "verification"

  testClassesDirs = sourceSets["integration"].output.classesDirs
  classpath = sourceSets["integration"].runtimeClasspath
  shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }

configure<com.bnorm.power.PowerAssertGradleExtension> {
  functions = listOf("com.bnorm.vegard.assertTrue")
}

tasks.processResources.configure {
  from(tasks.getByPath(":frontend:web:jsBundle")) {
    into("web-ui")
  }
}

application {
  mainClassName = "com.bnorm.vegard.MainKt"
  applicationDefaultJvmArgs = listOf("-Dkotlinx.coroutines.debug=on")
}
