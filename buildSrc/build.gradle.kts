plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    jcenter()   // JCenter is at end of life
}

object VersionIn {
    const val androidGradlePlugin = "7.0.0-alpha15"
    const val kotlin = "1.4.32"
    const val dokka = "1.4.20"
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${VersionIn.kotlin}")
    implementation("com.android.tools.build:gradle:7.1.0-alpha03")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:${VersionIn.dokka}")
    implementation("org.jetbrains.dokka:dokka-core:${VersionIn.dokka}")
}