import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
    id("com.diffplug.spotless") version "7.0.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "3.5.0"
    id("org.jetbrains.kotlin.plugin.spring") version "2.2.0-RC"
    kotlin("jvm") version "2.2.0-RC"
    kotlin("plugin.serialization") version "2.2.0-RC"
}

sourceSets { main { dependencies { implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6") } } }

repositories {
    mavenCentral()

    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    implementation("com.github.calimero:calimero-core:2.5")
    implementation("com.github.calimero:calimero-device:2.5")
    implementation("com.github.calimero:calimero-rxtx:2.5")
    implementation("com.luckycatlabs:SunriseSunsetCalculator:1.2")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.36")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:4.6.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

application {
    mainClass = "houseknxautomation.infrastructure.HouseKnxAutomationApplicationKt"
}

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

tasks.withType<Test> { useJUnitPlatform() }

group = "cx.mh"

version = "1.0-SNAPSHOT"
