plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "2.2.21"

    // JMH for microbenchmarking
    id("me.champeau.jmh") version "0.7.3"
}

jmh {
    // Explicitly set the core version to the latest
    jmhVersion.set("1.37")
}

group = "pt.isel"

repositories {
    mavenCentral()
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

dependencies {
    implementation(kotlin("reflect"))

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}