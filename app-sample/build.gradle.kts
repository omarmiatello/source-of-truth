plugins {
    id("com.android.application")
    kotlin("android")
    // kotlin("kapt")
}

android {
    compileSdkVersion(Sdk.compile)

    defaultConfig {
        minSdkVersion(Sdk.min)
        targetSdkVersion(Sdk.target)
        applicationId = "com.github.omarmiatello.sourceoftruth.appsample"
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = Version.java
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn" // for Jetpack Compose
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Lib.composeUi.version
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Version.java)
        targetCompatibility = JavaVersion.toVersion(Version.java)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        disable("ObsoleteLintCustomCheck")

        isWarningsAsErrors = true
        isAbortOnError = true
    }

    /**
     * Use this block to configure different flavors
     */
    // flavorDimensions("version")
    // productFlavors {
    //     create("full") {
    //         dimension = "version"
    //         applicationIdSuffix = ".full"
    //     }
    //     create("demo") {
    //         dimension = "version"
    //         applicationIdSuffix = ".demo"
    //     }
    // }
}

dependencies {
    implementation(Lib.androidxActivityKtx)
    implementation(Lib.androidxAppcompat)
    implementation(Lib.androidxCameraCamera2)
    implementation(Lib.androidxCameraCore)
    implementation(Lib.androidxCameraExtensions)
    implementation(Lib.androidxCameraLifecycle)
    implementation(Lib.androidxCameraView)
    implementation(Lib.androidxConstraintlayout)
    implementation(Lib.androidxCoreKtx)
    implementation(Lib.androidxFragmentKtx)
    implementation(Lib.androidxLifecycleRuntimeKtx)
    implementation(Lib.androidxLivedataKtx)
    implementation(Lib.androidxNavigationFragmentKtx)
    implementation(Lib.androidxNavigationUiKtx)
    implementation(Lib.androidxRecyclerview)
    implementation(Lib.androidxViewmodelKtx)

    implementation(Lib.composeAccompanistCoil)
    implementation(Lib.composeActivity)
    implementation(Lib.composeFoundation)
    implementation(Lib.composeIconsCore)
    implementation(Lib.composeIconsExt)
    implementation(Lib.composeMaterial)
    implementation(Lib.composeUi)
    implementation(Lib.composeUiTooling)
    implementation(Lib.composeViewmodel)

    implementation(Lib.coroutinesAndroid)
    implementation(Lib.coroutinesCore)
    implementation(Lib.coroutinesPlayServices)

    implementation(Lib.dagger)

    implementation(Lib.firebaseDatabaseKtx)
    implementation(Lib.firebaseStorageKtx)

    implementation(Lib.googleMaterial)

    implementation(Lib.hiltAndroid)
    implementation(Lib.hiltCommon)
    implementation(Lib.hiltViewModel)

    implementation(Lib.kotlinReflect)

    implementation(Lib.ktorClientOkhttp)

    implementation(Lib.mlkitBarcodeScanning)
    implementation(Lib.mlkitTextRecognition)

    // kapt(Lib.kaptDaggerCompiler)
    // kapt(Lib.kaptHiltAndroidCompiler)
    // kapt(Lib.kaptHiltCompiler)

    testImplementation(Lib.testAndroidxEspresso)
    testImplementation(Lib.testAndroidxExtJunit)
    testImplementation(Lib.testAndroidxRules)
    testImplementation(Lib.testAndroidxRunner)
    testImplementation(Lib.testJunit)
}
