version = "1.0.1"
description = "Handle multiple sources (local / remote) and build a \"single source of truth\""

plugins {
    id("java-library")
    kotlin("jvm")
    id("maven-publish")
    publish
}

kotlin {
    explicitApi()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = Version.java
}

dependencies {
    implementation(Lib.coroutinesCore)

    testImplementation(Lib.testJunit)
}

java {
    sourceCompatibility = JavaVersion.toVersion(Version.java)
    targetCompatibility = JavaVersion.toVersion(Version.java)
}
