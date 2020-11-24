import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
}

dependencies {
    implementation(project(":model"))
    implementation(project(":data:repository"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx)
    implementation(Deps.AndroidX.Room.runtime)
    implementation(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)

    testImplementation(Deps.JUnit.jupiterApi)
    testImplementation(Deps.JUnit.jupiterEngine)
    testImplementation(Deps.Spek.dslJvm)
    testImplementation(Deps.Spek.junit5)
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
