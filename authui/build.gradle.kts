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

    implementation(Deps.AndroidX.Lifecycle.runtime)
    implementation(Deps.Kotlin.coroutinesPlayServices)
    implementation(Deps.Firebase.auth)
    implementation(Deps.gmsAuth)
    implementation(Deps.facebookLogin)

    implementation(Deps.Dagger.Hilt.android)
    kapt(Deps.Dagger.Hilt.compiler)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
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
