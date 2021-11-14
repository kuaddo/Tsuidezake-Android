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
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx)
    implementation(Deps.inject)
    implementation(Deps.Dagger.Hilt.android)
    kapt(Deps.Dagger.Hilt.compiler)

    testImplementation(Deps.Test.AndroidX.coreTesting)
    testImplementation(Deps.Test.AndroidX.jUnit)
    testImplementation(Deps.Test.assertJ)
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
