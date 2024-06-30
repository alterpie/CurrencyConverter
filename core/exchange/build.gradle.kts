plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.example.currencyconverter.core.exchange"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.hilt)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.androidx.datastore)

    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
}