import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun String.isStableVersion(): Boolean {
    val upperCase = toUpperCase(java.util.Locale.ROOT)
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { it in upperCase }
    return stableKeyword || Regex("^[0-9,.v-]+(-r)?$").matches(this)
}

fun String.isNotStableVersion() = !isStableVersion()

val String.version get() = substringAfterLast(":")

object Sdk {
    const val min = 21
    const val target = 30
    const val compile = 30
}

object Version {
    const val java = "11"
    const val dokka = "1.4.20"
    const val ktlint = "0.41.0"
}

object Lib {
    const val androidxActivityKtx = "androidx.activity:activity-ktx:1.9.0-alpha03"
    const val androidxAppcompat = "androidx.appcompat:appcompat:1.7.0-alpha03"
    const val androidxCameraCamera2 = "androidx.camera:camera-camera2:1.1.0-alpha04"
    const val androidxCameraCore = "androidx.camera:camera-core:1.1.0-alpha04"
    const val androidxCameraExtensions = "androidx.camera:camera-extensions:1.0.0-alpha24"
    const val androidxCameraLifecycle = "androidx.camera:camera-lifecycle:1.1.0-alpha04"
    const val androidxCameraView = "androidx.camera:camera-view:1.0.0-alpha24"
    const val androidxConstraintlayout = "androidx.constraintlayout:constraintlayout:2.1.0-beta01"
    const val androidxCoreKtx = "androidx.core:core-ktx:1.13.0-alpha05"
    const val androidxFragmentKtx = "androidx.fragment:fragment-ktx:1.7.0-alpha10"
    const val androidxLifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.8.0-alpha01"
    const val androidxLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha01"
    const val androidxNavigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:2.3.5"
    const val androidxNavigationUiKtx = "androidx.navigation:navigation-ui-ktx:2.3.5"
    const val androidxRecyclerview = "androidx.recyclerview:recyclerview:1.2.0"
    const val androidxViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0-alpha01"
    const val composeAccompanistCoil = "com.google.accompanist:accompanist-coil:0.15.0"
    const val composeActivity = "androidx.activity:activity-compose:1.9.0-alpha03"
    const val composeFoundation = "androidx.compose.foundation:foundation:1.7.0-alpha02"
    const val composeIconsCore = "androidx.compose.material:material-icons-core:1.7.0-alpha02"
    const val composeIconsExt = "androidx.compose.material:material-icons-extended:1.7.0-alpha02"
    const val composeMaterial = "androidx.compose.material:material:1.7.0-alpha02"
    const val composeUi = "androidx.compose.ui:ui:1.7.0-alpha02"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:1.7.0-alpha02"
    const val composeViewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-alpha01"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0-RC2"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"
    const val coroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.0-RC"
    const val dagger = "com.google.dagger:dagger:2.35.1"
    const val firebaseDatabaseKtx = "com.google.firebase:firebase-database-ktx:19.7.0"
    const val firebaseStorageKtx = "com.google.firebase:firebase-storage-ktx:19.2.2"
    const val googleMaterial = "com.google.android.material:material:1.4.0-alpha02"
    const val hiltAndroid = "com.google.dagger:hilt-android:2.35.1"
    const val hiltCommon = "androidx.hilt:hilt-common:1.0.0"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0"
    const val kaptDaggerCompiler = "com.google.dagger:dagger-compiler:2.35.1"
    const val kaptHiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:2.35.1"
    const val kaptHiltCompiler = "androidx.hilt:hilt-compiler:1.0.0"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:2.0.0-Beta4"
    const val ktorClientOkhttp = "io.ktor:ktor-client-okhttp:1.5.4"
    const val mlkitBarcodeScanning = "com.google.mlkit:barcode-scanning:16.1.1"
    const val mlkitTextRecognition = "com.google.android.gms:play-services-mlkit-text-recognition:16.1.3"
    const val testAndroidxEspresso = "androidx.test.espresso:espresso-core:3.6.0-alpha03"
    const val testAndroidxExtJunit = "androidx.test.ext:junit:1.2.0-alpha03"
    const val testAndroidxRules = "androidx.test:rules:1.6.0-alpha03"
    const val testAndroidxRunner = "androidx.test:runner:1.6.0-alpha06"
    const val testJunit = "junit:junit:4.13.2"
}

// Project modules
val DependencyHandler.moduleCore get() = project(":core")
