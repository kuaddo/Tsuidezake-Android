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
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx) // TODO: remove
    implementation(Deps.AndroidX.annotation)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
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
