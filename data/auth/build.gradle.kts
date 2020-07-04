import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion(Deps.buildToolsVersion)
    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        buildConfig = false
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.coroutinesPlayServices)
    implementation(Deps.Firebase.auth)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
    implementation(Deps.timber)
}