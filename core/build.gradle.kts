plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("com.github.ben-manes.versions")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(Deps.Kotlin.stdlib)
    api(Deps.AndroidX.Lifecycle.liveDataKtx)
    api(Deps.inject)
}
