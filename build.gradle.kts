plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.vmlakk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
}
kotlin {
    jvmToolchain(21)
}
