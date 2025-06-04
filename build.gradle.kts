import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    application
}

sourceSets {
    main {
        dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3")
        }
    }
}

group = "me.mhert"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    implementation("com.github.calimero:calimero-core:2.5")
    implementation("com.github.calimero:calimero-device:2.5")
    implementation("com.github.calimero:calimero-rxtx:2.5")
    implementation("com.luckycatlabs:SunriseSunsetCalculator:1.2")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.36")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.mockito:mockito-core:4.6.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_15.toString()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

application {
    mainClass = "MainKt"
}

