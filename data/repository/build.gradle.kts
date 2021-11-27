import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(project(":core"))
    implementation(project(":model"))
    implementation(project(":domain"))
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx) // TODO: remove
    implementation(Deps.AndroidX.annotation)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
    implementation(Deps.Dagger.Hilt.android)
    kapt(Deps.Dagger.Hilt.compiler)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        getRejectVersion(candidate, currentVersion)
    }
    outputFormatter = dependencyUpdatesFormatter
}

ktlint {
    android.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}
