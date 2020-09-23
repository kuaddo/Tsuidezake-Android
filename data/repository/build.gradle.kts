import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(project(":model"))
    implementation(project(":domain"))
    implementation(project(":data:remote"))
    compileOnly(project(":data:auth"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

ktlint {
    android.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}
