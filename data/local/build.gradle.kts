plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.github.ben-manes.versions")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(project(":core"))
    implementation(project(":model"))

    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.AndroidX.coreKtx)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
}
