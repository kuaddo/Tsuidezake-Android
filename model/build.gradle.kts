import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

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
