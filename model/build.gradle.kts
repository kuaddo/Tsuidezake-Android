import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(Deps.Kotlin.stdlib)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}
