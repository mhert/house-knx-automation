import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val calimero = "3.0-SNAPSHOT"
val junitJupiter = "5.12.1"
val kotlinxCoroutinesReactor = "1.10.2"
val kotlinxSerializationJson = "1.8.1"
val mockitoKotlin = "5.4.0"
val mockitoCore = "5.14.2"
val sunriseSunsetCalculator = "1.2"

plugins {
    id("application")
    id("com.diffplug.spotless") version "7.0.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "3.5.0"
    id("org.jetbrains.kotlin.plugin.spring") version "2.2.0-RC"
    kotlin("jvm") version "2.2.0-RC"
    kotlin("plugin.serialization") version "2.2.0-RC"
}

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    implementation("io.calimero", "calimero-core", calimero)
    implementation("io.calimero", "calimero-device", calimero)
    implementation("org.jetbrains.kotlin", "kotlin-reflect")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", kotlinxCoroutinesReactor)
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", kotlinxSerializationJson)
    implementation("org.springframework.boot", "spring-boot-starter")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-aop")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("com.luckycatlabs", "SunriseSunsetCalculator", sunriseSunsetCalculator)
    runtimeOnly("io.calimero", "calimero-rxtx", calimero)
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitJupiter)
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", mockitoKotlin)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitJupiter)
    mockitoAgent("org.mockito", "mockito-core", mockitoCore) { isTransitive = false }
}

repositories {
    mavenCentral()

    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

application { mainClass = "houseknxautomation.infrastructure.HouseKnxAutomationApplicationKt" }

spotless {
    kotlin {
        target("**/*.kt", "**/*.kts")
        ktfmt().kotlinlangStyle()
    }

    yaml {
        target("**/*.yaml", "**/*.yml")
        jackson()
    }

    format("misc") {
        target("**/*.gitignore", "**/*.properties", "**/*.md", "LICENSE")

        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = freeCompilerArgs.get() + listOf("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_24
    }
}

tasks { test { jvmArgs?.add("-javaagent:${mockitoAgent.asPath}") } }

tasks.withType<Test> { useJUnitPlatform() }

group = "cx.mh"

version = "1.0-SNAPSHOT"
