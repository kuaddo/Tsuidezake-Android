import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(project(":model"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    compileOnly(project(":data:auth"))
    implementation(Deps.Kotlin.stdlib)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}
