import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.coroutinesPlayServices)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx)
    implementation(Deps.Firebase.auth)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
    implementation(Deps.timber)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}
