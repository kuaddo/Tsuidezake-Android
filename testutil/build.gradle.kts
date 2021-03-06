import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx)
    implementation(Deps.Test.kotlinCoroutinesTest)
    implementation(Deps.Test.AndroidX.coreTesting)
    implementation(Deps.Test.AndroidX.testRunner)
    implementation(Deps.Test.Spek.dslJvm)
    implementation(Deps.Test.mockk)
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
